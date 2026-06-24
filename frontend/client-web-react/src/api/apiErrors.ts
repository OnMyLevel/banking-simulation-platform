export type GatewayApiError = {
  status: number;
  message: string;
  code?: string;
  correlationId?: string;
  retryAfterSeconds?: number;
};

export class GatewayApiException extends Error {
  readonly status: number;
  readonly code?: string;
  readonly correlationId?: string;
  readonly retryAfterSeconds?: number;

  constructor(error: GatewayApiError) {
    super(error.message);
    this.name = 'GatewayApiException';
    this.status = error.status;
    this.code = error.code;
    this.correlationId = error.correlationId;
    this.retryAfterSeconds = error.retryAfterSeconds;
  }
}

export function isGatewayApiException(error: unknown): error is GatewayApiException {
  return error instanceof GatewayApiException;
}

export function messageForStatus(status: number): string {
  if (status === 400) {
    return 'The request is invalid.';
  }
  if (status === 401) {
    return 'Please sign in again.';
  }
  if (status === 403) {
    return 'You are not allowed to access this resource.';
  }
  if (status === 404) {
    return 'The requested resource was not found.';
  }
  if (status === 429) {
    return 'Too many requests. Please try again later.';
  }
  if (status >= 500) {
    return 'A technical error occurred. Please try again later.';
  }
  return 'Unexpected API response.';
}

export function retryAfterSeconds(headers: Headers): number | undefined {
  const value = headers.get('Retry-After');
  if (!value) {
    return undefined;
  }
  const parsed = Number.parseInt(value, 10);
  return Number.isNaN(parsed) ? undefined : parsed;
}
