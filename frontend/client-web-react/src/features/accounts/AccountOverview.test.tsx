import { act } from 'react';
import { createRoot } from 'react-dom/client';
import { afterEach, describe, expect, it } from 'vitest';
import { GatewayApiException } from '../../api/apiErrors';
import { AccountOverview } from './AccountOverview';

let container: HTMLDivElement | undefined;

afterEach(() => {
  container?.remove();
  container = undefined;
});

describe('AccountOverview', () => {
  it('renders account details when the account is loaded', async () => {
    container = document.createElement('div');
    document.body.append(container);
    const root = createRoot(container);

    await act(async () => {
      root.render(
        <AccountOverview
          loadAccount={() =>
            Promise.resolve({
              data: {
                accountId: 'acc-123',
                balance: 1250,
                currency: 'EUR',
                status: 'ACTIVE',
              },
              correlationId: 'corr-123',
            })
          }
        />,
      );
    });

    expect(container.textContent).toContain('Primary account');
    expect(container.textContent).toContain('acc-123');
    expect(container.textContent).toContain('€1,250.00');
    expect(container.textContent).toContain('corr-123');
  });

  it('renders normalized API errors with support reference', async () => {
    container = document.createElement('div');
    document.body.append(container);
    const root = createRoot(container);

    await act(async () => {
      root.render(
        <AccountOverview
          loadAccount={() =>
            Promise.reject(
              new GatewayApiException({
                status: 429,
                message: 'Too many requests. Please try again later.',
                correlationId: 'corr-429',
                retryAfterSeconds: 30,
              }),
            )
          }
        />,
      );
    });

    expect(container.textContent).toContain('Account unavailable');
    expect(container.textContent).toContain('Too many requests. Please try again later.');
    expect(container.textContent).toContain('Retry after 30 seconds.');
    expect(container.textContent).toContain('corr-429');
  });
});
