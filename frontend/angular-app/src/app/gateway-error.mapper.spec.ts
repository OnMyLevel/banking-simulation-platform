import { toAngularGatewayErrorState } from './gateway-error.mapper';

describe('toAngularGatewayErrorState', () => {
  it('maps access denied errors', () => {
    const state = toAngularGatewayErrorState({
      status: 403,
      message: 'Forbidden',
      correlationId: 'corr-ng-403',
    });

    expect(state.title).toBe('Access denied');
    expect(state.reference).toBe('corr-ng-403');
  });

  it('maps throttling errors with retry delay', () => {
    const state = toAngularGatewayErrorState({
      status: 429,
      message: 'Too many requests. Please try again later.',
      correlationId: 'corr-ng-429',
      retryAfterSeconds: 45,
    });

    expect(state.title).toBe('Too many requests');
    expect(state.retryAfterSeconds).toBe(45);
  });

  it('maps technical errors', () => {
    const state = toAngularGatewayErrorState({
      status: 502,
      message: 'A technical error occurred. Please try again later.',
    });

    expect(state.title).toBe('Technical error');
  });
});
