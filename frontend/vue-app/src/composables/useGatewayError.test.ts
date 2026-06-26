import { describe, expect, it } from 'vitest';
import { toVueGatewayErrorState } from './useGatewayError';

describe('toVueGatewayErrorState', () => {
  it('maps access denied errors', () => {
    const state = toVueGatewayErrorState({
      status: 403,
      message: 'Forbidden',
      correlationId: 'corr-vue-403',
    });

    expect(state.title).toBe('Access denied');
    expect(state.reference).toBe('corr-vue-403');
  });

  it('maps throttling errors with retry delay', () => {
    const state = toVueGatewayErrorState({
      status: 429,
      message: 'Too many requests. Please try again later.',
      correlationId: 'corr-vue-429',
      retryAfterSeconds: 45,
    });

    expect(state.title).toBe('Too many requests');
    expect(state.retryAfterSeconds).toBe(45);
  });

  it('maps technical errors', () => {
    const state = toVueGatewayErrorState({
      status: 502,
      message: 'A technical error occurred. Please try again later.',
    });

    expect(state.title).toBe('Technical error');
  });
});
