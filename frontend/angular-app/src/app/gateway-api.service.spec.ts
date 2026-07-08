import { GatewayApiService, type GatewayFetch } from './gateway-api.service';

describe('GatewayApiService', () => {
  it('loads advisor dashboard data', async () => {
    const service = new GatewayApiService(
      responseWith({
        title: 'Advisor operations',
        items: [{ label: 'Support cases', value: '4 open' }],
      }),
    );

    const result = await service.loadAdvisorDashboard();

    expect(result.status).toBe('ready');
    if (result.status === 'ready') {
      expect(result.data.title).toBe('Advisor operations');
      expect(result.data.items[0].value).toBe('4 open');
    }
  });

  it('maps empty responses', async () => {
    const service = new GatewayApiService(responseWith(null, { status: 204 }));

    const result = await service.loadAdvisorDashboard();

    expect(result.status).toBe('empty');
  });

  it('maps empty item responses', async () => {
    const service = new GatewayApiService(
      responseWith({
        title: 'Advisor operations',
        items: [],
      }),
    );

    const result = await service.loadAdvisorDashboard();

    expect(result.status).toBe('empty');
  });

  it('maps gateway errors through the shared error mapper', async () => {
    const service = new GatewayApiService(
      responseWith(
        {
          status: 429,
          message: 'Too many requests. Please try again later.',
          correlationId: 'corr-angular-429',
        },
        {
          status: 429,
          headers: { 'retry-after': '30' },
        },
      ),
    );

    const result = await service.loadAdvisorDashboard();

    expect(result.status).toBe('error');
    if (result.status === 'error') {
      expect(result.error.title).toBe('Too many requests');
      expect(result.error.reference).toBe('corr-angular-429');
      expect(result.error.retryAfterSeconds).toBe(30);
    }
  });
});

function responseWith(body: unknown, init: ResponseInit = {}): GatewayFetch {
  return async () =>
    new Response(body === null ? null : JSON.stringify(body), {
      status: init.status ?? 200,
      headers: {
        'content-type': 'application/json',
        ...(init.headers as Record<string, string> | undefined),
      },
    });
}
