import type { FrontendApiError } from '../../../common-types/src';
import {
  shouldAskForSignIn,
  shouldShowAccessDenied,
  shouldShowNotFound,
  shouldShowTechnicalMessage,
  shouldShowThrottleMessage,
} from '../../../common-types/src';

export type AngularGatewayErrorState = {
  title: string;
  message: string;
  reference?: string;
  retryAfterSeconds?: number;
};

export function toAngularGatewayErrorState(error: FrontendApiError): AngularGatewayErrorState {
  if (shouldAskForSignIn(error.status)) {
    return buildState(error, 'Session expired', 'Please sign in again.');
  }
  if (shouldShowAccessDenied(error.status)) {
    return buildState(error, 'Access denied', 'You are not allowed to access this resource.');
  }
  if (shouldShowNotFound(error.status)) {
    return buildState(error, 'Advisor data not found', 'The requested advisor data was not found.');
  }
  if (shouldShowThrottleMessage(error.status)) {
    return buildState(error, 'Too many requests', error.message);
  }
  if (shouldShowTechnicalMessage(error.status)) {
    return buildState(error, 'Technical error', error.message);
  }
  return buildState(error, 'Unexpected response', error.message);
}

function buildState(error: FrontendApiError, title: string, message: string): AngularGatewayErrorState {
  return {
    title,
    message,
    reference: error.correlationId,
    retryAfterSeconds: error.retryAfterSeconds,
  };
}
