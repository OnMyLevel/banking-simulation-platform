import { Injectable, InjectionToken, inject } from '@angular/core';
import type { FrontendApiError } from '../../../common-types/src';
import { environment } from '../environments/environment';
import { toAngularGatewayErrorState, type AngularGatewayErrorState } from './gateway-error.mapper';

export type AdvisorDashboardData = {
  title: string;
  items: Array<{
    label: string;
    value: string;
  }>;
};

export type AdvisorDashboardResult =
  | {
      status: 'ready';
      data: AdvisorDashboardData;
    }
  | {
      status: 'empty';
    }
  | {
      status: 'error';
      error: AngularGatewayErrorState;
    };

export type GatewayFetch = (input: string, init?: RequestInit) => Promise<Response>;

export const GATEWAY_FETCH = new InjectionToken<GatewayFetch>('GATEWAY_FETCH', {
  providedIn: 'root',
  factory: () => fetch,
});

@Injectable({ providedIn: 'root' })
export class GatewayApiService {
  private readonly advisorDashboardUrl = `${environment.gatewayBaseUrl}${environment.advisorDashboardPath}`;
  private readonly gatewayFetch = inject(GATEWAY_FETCH);

  async loadAdvisorDashboard(): Promise<AdvisorDashboardResult> {
    const response = await this.gatewayFetch(this.advisorDashboardUrl, {
      headers: {
        Accept: 'application/json',
      },
    });

    if (response.status === 204) {
      return { status: 'empty' };
    }

    if (!response.ok) {
      return {
        status: 'error',
        error: toAngularGatewayErrorState(await this.readApiError(response)),
      };
    }

    const data = (await response.json()) as AdvisorDashboardData;

    if (data.items.length === 0) {
      return { status: 'empty' };
    }

    return {
      status: 'ready',
      data,
    };
  }

  private async readApiError(response: Response): Promise<FrontendApiError> {
    const fallbackMessage = 'A technical error occurred. Please try again later.';

    try {
      const body = (await response.json()) as Partial<FrontendApiError>;
      return {
        status: response.status,
        message: body.message ?? fallbackMessage,
        correlationId: body.correlationId ?? response.headers.get('x-correlation-id') ?? undefined,
        retryAfterSeconds: body.retryAfterSeconds ?? this.readRetryAfterSeconds(response),
      };
    } catch {
      return {
        status: response.status,
        message: fallbackMessage,
        correlationId: response.headers.get('x-correlation-id') ?? undefined,
        retryAfterSeconds: this.readRetryAfterSeconds(response),
      };
    }
  }

  private readRetryAfterSeconds(response: Response): number | undefined {
    const retryAfter = response.headers.get('retry-after');

    if (!retryAfter) {
      return undefined;
    }

    const parsedRetryAfter = Number(retryAfter);
    return Number.isFinite(parsedRetryAfter) ? parsedRetryAfter : undefined;
  }
}
