import type { FrontendApiError } from '../../../common-types/src';
import {
  shouldAskForSignIn,
  shouldShowAccessDenied,
  shouldShowNotFound,
  shouldShowTechnicalMessage,
  shouldShowThrottleMessage,
} from '../../../common-types/src';

export type VueGatewayErrorState = {
  title: string;
  message: string;
  reference?: string;
  retryAfterSeconds?: number;
};

export function toVueGatewayErrorState(error: FrontendApiError): VueGatewayErrorState {
  if (shouldAskForSignIn(error.status)) {
    return buildState(error, 'Session expired', 'Please sign in again.');
  }
  if (shouldShowAccessDenied(error.status)) {
    return buildState(error, 'Access denied', 'You are not allowed to access this resource.');
  }
  if (shouldShowNotFound(error.status)) {
    return buildState(error, 'Company data not found', 'The requested company data was not found.');
  }
  if (shouldShowThrottleMessage(error.status)) {
    return buildState(error, 'Too many requests', error.message);
  }
  if (shouldShowTechnicalMessage(error.status)) {
    return buildState(error, 'Technical error', error.message);
  }
  return buildState(error, 'Unexpected response', error.message);
}

function buildState(error: FrontendApiError, title: string, message: string): VueGatewayErrorState {
  return {
    title,
    message,
    reference: error.correlationId,
    retryAfterSeconds: error.retryAfterSeconds,
  };
}
