import { describe, expect, it } from 'vitest';
import {
  isClientError,
  isServerError,
  shouldAskForSignIn,
  shouldShowAccessDenied,
  shouldShowTechnicalMessage,
  shouldShowThrottleMessage,
} from './status';

describe('status helpers', () => {
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
    expect(shouldShowThrottleMessage(429)).toBe(true);
    expect(shouldShowTechnicalMessage(502)).toBe(true);
  });
});
