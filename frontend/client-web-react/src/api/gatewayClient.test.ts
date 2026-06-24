import { describe, expect, it, vi } from 'vitest';
import { GatewayApiException } from './apiErrors';
import { GatewayClient } from './gatewayClient';

describe('GatewayClient', () => {
  it('adds authorization and correlation headers', async () => {
    const fetcher = vi.fn().mockResolvedValue(jsonResponse({ ok: true }, 200, 'correlation-1'));
    const client = new GatewayClient({
      baseUrl: 'http://localhost:8080',
      getAccessValue: () => 'access-1',
      createCorrelationId: () => 'correlation-1',
      fetcher,
    });

    await client.request({ method: 'GET', path: '/api/accounts/123' });

    const [, options] = fetcher.mock.calls[0];
    expect(options.headers.get('Authorization')).toBe('Bearer access-1');
    expect(options.headers.get('X-Correlation-Id')).toBe('correlation-1');
  });

  it('normalizes api errors', async () => {
    const fetcher = vi.fn().mockResolvedValue(jsonResponse({ message: 'Too many requests' }, 429, 'correlation-2', '60'));
    const client = new GatewayClient({ baseUrl: 'http://localhost:8080', fetcher });

    await expect(client.request({ method: 'GET', path: '/api/accounts/123' })).rejects.toMatchObject({
      status: 429,
      message: 'Too many requests',
      correlationId: 'correlation-2',
      retryAfterSeconds: 60,
    } satisfies Partial<GatewayApiException>);
  });
});

function jsonResponse(body: unknown, status: number, correlationId: string, retryAfter?: string): Response {
  const headers = new Headers({ 'X-Correlation-Id': correlationId });
  if (retryAfter) {
    headers.set('Retry-After', retryAfter);
  }
  return new Response(JSON.stringify(body), { status, headers });
}
