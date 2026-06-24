import { GatewayClient, GatewayResponse } from './gatewayClient';

export type AccountDto = {
  accountId: string;
  balance?: number;
  currency?: string;
  status?: string;
};

export function createAccountsApi(gatewayClient: GatewayClient) {
  return {
    getAccount(accountId: string): Promise<GatewayResponse<AccountDto>> {
      return gatewayClient.request<AccountDto>({
        method: 'GET',
        path: `/api/accounts/${encodeURIComponent(accountId)}`,
      });
    },

    listAccounts(): Promise<GatewayResponse<AccountDto[]>> {
      return gatewayClient.request<AccountDto[]>({
        method: 'GET',
        path: '/api/accounts',
      });
    },
  };
}

export type AccountsApi = ReturnType<typeof createAccountsApi>;
