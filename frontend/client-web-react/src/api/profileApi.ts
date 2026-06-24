import { GatewayClient, GatewayResponse } from './gatewayClient';

export type ProfileDto = {
  id: string;
  email?: string;
  status?: string;
};

export function createProfileApi(gatewayClient: GatewayClient) {
  return {
    getProfile(): Promise<GatewayResponse<ProfileDto>> {
      return gatewayClient.request<ProfileDto>({
        method: 'GET',
        path: '/api/users/me',
      });
    },
  };
}

export type ProfileApi = ReturnType<typeof createProfileApi>;
