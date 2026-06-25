import type { FrontendApiError } from '../../../common-types/src';
import {
  shouldAskForSignIn,
  shouldShowAccessDenied,
  shouldShowNotFound,
  shouldShowTechnicalMessage,
  shouldShowThrottleMessage,
} from '../../../common-types/src';

export type GatewayApiError = FrontendApiError;

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
  if (shouldAskForSignIn(status)) {
    return 'Please sign in again.';
  }
  if (shouldShowAccessDenied(status)) {
    return 'You are not allowed to access this resource.';
  }
  if (shouldShowNotFound(status)) {
    return 'The requested resource was not found.';
  }
  if (shouldShowThrottleMessage(status)) {
    return 'Too many requests. Please try again later.';
  }
  if (shouldShowTechnicalMessage(status)) {
    return 'A technical error occurred. Please try again later.';
  }
  return 'Unexpected API response.';
}
