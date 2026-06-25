import { useEffect, useState } from 'react';
import type { AccountDto } from '../../api/accountsApi';
import type { GatewayResponse } from '../../api/gatewayClient';
import { isGatewayApiException } from '../../api/apiErrors';

export type AccountOverviewProps = {
  loadAccount: () => Promise<GatewayResponse<AccountDto>>;
};

type AccountOverviewState =
  | { status: 'loading' }
  | { status: 'success'; account: AccountDto; correlationId?: string }
  | { status: 'error'; message: string; correlationId?: string; retryAfterSeconds?: number };

export function AccountOverview({ loadAccount }: AccountOverviewProps) {
  const [state, setState] = useState<AccountOverviewState>({ status: 'loading' });

  useEffect(() => {
    let mounted = true;

    async function load() {
      try {
        const response = await loadAccount();
        if (mounted) {
          setState({
            status: 'success',
            account: response.data,
            correlationId: response.correlationId,
          });
        }
      } catch (error) {
        if (!mounted) {
          return;
        }
        if (isGatewayApiException(error)) {
          setState({
            status: 'error',
            message: error.message,
            correlationId: error.correlationId,
            retryAfterSeconds: error.retryAfterSeconds,
          });
          return;
        }
        setState({
          status: 'error',
          message: 'Unable to load account overview.',
        });
      }
    }

    void load();

    return () => {
      mounted = false;
    };
  }, [loadAccount]);

  if (state.status === 'loading') {
    return (
      <section className="account-card" aria-busy="true">
        <p className="eyebrow">Account overview</p>
        <h1>Loading your account</h1>
        <p>We are preparing the latest account information.</p>
      </section>
    );
  }

  if (state.status === 'error') {
    return (
      <section className="account-card error-card" role="alert">
        <p className="eyebrow">Account overview</p>
        <h1>Account unavailable</h1>
        <p>{state.message}</p>
        {state.retryAfterSeconds ? <p>Retry after {state.retryAfterSeconds} seconds.</p> : null}
        {state.correlationId ? <p className="support-ref">Reference: {state.correlationId}</p> : null}
      </section>
    );
  }

  const balance = formatBalance(state.account.balance, state.account.currency);

  return (
    <section className="account-card">
      <p className="eyebrow">Account overview</p>
      <h1>Primary account</h1>
      <dl className="account-details">
        <div>
          <dt>Account id</dt>
          <dd>{state.account.accountId}</dd>
        </div>
        <div>
          <dt>Balance</dt>
          <dd>{balance}</dd>
        </div>
        <div>
          <dt>Status</dt>
          <dd>{state.account.status ?? 'Unknown'}</dd>
        </div>
      </dl>
      {state.correlationId ? <p className="support-ref">Reference: {state.correlationId}</p> : null}
    </section>
  );
}

function formatBalance(balance: number | undefined, currency: string | undefined): string {
  if (balance === undefined) {
    return 'Not available';
  }
  return new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: currency ?? 'EUR',
  }).format(balance);
}
