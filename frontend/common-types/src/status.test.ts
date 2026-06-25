import { describe, expect, it } from 'vitest';
import {
  isClientError,
  isServerError,
  isSuccess,
  shouldAskForSignIn,
  shouldShowAccessDenied,
  shouldShowNotFound,
  shouldShowTechnicalMessage,
  shouldShowThrottleMessage,
} from './status';

describe('status helpers', () => {
  it('classifies success responses', () => {
    expect(isSuccess(200)).toBe(true);
    expect(isSuccess(204)).toBe(true);
    expect(isSuccess(300)).toBe(false);
  });

  it('classifies client errors', () => {
    expect(isClientError(400)).toBe(true);
    expect(isClientError(404)).toBe(true);
    expect(isClientError(500)).toBe(false);
  });

  it('classifies server errors', () => {
    expect(isServerError(500)).toBe(true);
    expect(isServerError(503)).toBe(true);
    expect(isServerError(404)).toBe(false);
  });

  it('maps known UI states', () => {
    expect(shouldAskForSignIn(401)).toBe(true);
    expect(shouldShowAccessDenied(403)).toBe(true);
    expect(shouldShowNotFound(404)).toBe(true);
    expect(shouldShowThrottleMessage(429)).toBe(true);
    expect(shouldShowTechnicalMessage(502)).toBe(true);
  });
});
