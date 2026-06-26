import { CLIENT_FIELD_NAMES, parseRetrySeconds } from '../../../common-types/src';
import { GatewayApiException, messageForStatus } from './apiErrors';

export type GatewayClientOptions = {
  baseUrl: string;
  getAccessValue?: () => string | undefined;
  createCorrelationId?: () => string;
  fetcher?: typeof fetch;
};

export type GatewayRequestOptions = {
  method?: 'GET' | 'POST' | 'PUT' | 'PATCH' | 'DELETE';
  path: string;
  body?: unknown;
  correlationId?: string;
};

export type GatewayResponse<T> = {
  data: T;
  correlationId?: string;
};

export class GatewayClient {
  private readonly baseUrl: string;
  private readonly getAccessValue?: () => string | undefined;
  private readonly createCorrelationId?: () => string;
  private readonly fetcher: typeof fetch;

  constructor(options: GatewayClientOptions) {
    this.baseUrl = options.baseUrl.replace(/\/$/, '');
    this.getAccessValue = options.getAccessValue;
    this.createCorrelationId = options.createCorrelationId;
    this.fetcher = options.fetcher ?? fetch;
  }

  async request<T>(options: GatewayRequestOptions): Promise<GatewayResponse<T>> {
    const method = options.method ?? 'GET';
    const headers = new Headers();
    const accessValue = this.getAccessValue?.();
    const correlationId = options.correlationId ?? this.createCorrelationId?.();

    if (options.body !== undefined) {
      headers.set('Content-Type', 'application/json');
    }
    if (accessValue) {
      headers.set(CLIENT_FIELD_NAMES.access, `Bearer ${accessValue}`);
    }
    if (correlationId) {
      headers.set(CLIENT_FIELD_NAMES.trace, correlationId);
    }

    const response = await this.fetcher(`${this.baseUrl}${options.path}`, {
      method,
      headers,
      body: options.body === undefined ? undefined : JSON.stringify(options.body),
    });

    const responseCorrelationId = response.headers.get(CLIENT_FIELD_NAMES.trace) ?? undefined;

    if (!response.ok) {
      throw new GatewayApiException({
        status: response.status,
        message: await this.errorMessage(response),
        correlationId: responseCorrelationId,
        retryAfterSeconds: parseRetrySeconds(response.headers.get(CLIENT_FIELD_NAMES.retry)),
      });
    }

    return {
      data: await this.parseBody<T>(response),
      correlationId: responseCorrelationId,
    };
  }

  private async parseBody<T>(response: Response): Promise<T> {
    if (response.status === 204) {
      return undefined as T;
    }
    const text = await response.text();
    if (!text) {
      return undefined as T;
    }
    return JSON.parse(text) as T;
  }

  private async errorMessage(response: Response): Promise<string> {
    try {
      const text = await response.text();
      if (!text) {
        return messageForStatus(response.status);
      }
      const payload = JSON.parse(text) as { message?: string };
      return payload.message ?? messageForStatus(response.status);
    } catch {
      return messageForStatus(response.status);
    }
  }
}
