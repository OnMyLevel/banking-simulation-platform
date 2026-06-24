import { GatewayClient, GatewayResponse } from './gatewayClient';

export type MoneyCommand = {
  accountId: string;
  amount: number;
  currency: string;
};

export type TransferCommand = {
  sourceAccountId: string;
  targetAccountId: string;
  amount: number;
  currency: string;
};

export type OperationDto = {
  operationId: string;
  accountId?: string;
  amount?: number;
  currency?: string;
  type?: string;
  status?: string;
};

export function createOperationsApi(gatewayClient: GatewayClient) {
  return {
    createCredit(command: MoneyCommand): Promise<GatewayResponse<OperationDto>> {
      return gatewayClient.request<OperationDto>({
        method: 'POST',
        path: '/api/operations/credits',
        body: command,
      });
    },

    createDebit(command: MoneyCommand): Promise<GatewayResponse<OperationDto>> {
      return gatewayClient.request<OperationDto>({
        method: 'POST',
        path: '/api/operations/debits',
        body: command,
      });
    },

    createTransfer(command: TransferCommand): Promise<GatewayResponse<OperationDto>> {
      return gatewayClient.request<OperationDto>({
        method: 'POST',
        path: '/api/operations/transfers',
        body: command,
      });
    },
  };
}

export type OperationsApi = ReturnType<typeof createOperationsApi>;
