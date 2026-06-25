import { useMemo } from 'react';
import { createAccountsApi } from './api/accountsApi';
import { GatewayClient } from './api/gatewayClient';
import { AccountOverview } from './features/accounts/AccountOverview';

const demoAccountId = '00000000-0000-0000-0000-000000000000';

export function App() {
  const accountsApi = useMemo(() => {
    const gatewayClient = new GatewayClient({
      baseUrl: import.meta.env.VITE_GATEWAY_BASE_URL ?? 'http://localhost:8080',
      createCorrelationId: () => crypto.randomUUID(),
    });

    return createAccountsApi(gatewayClient);
  }, []);

  return (
    <main className="app-shell">
      <AccountOverview loadAccount={() => accountsApi.getAccount(demoAccountId)} />
    </main>
  );
}
