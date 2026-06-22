import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  vus: Number(__ENV.K6_VUS || 5),
  duration: __ENV.K6_DURATION || '30s',
  thresholds: {
    http_req_failed: ['rate<0.05'],
    http_req_duration: ['p(95)<750']
  }
};

const accountApiBaseUrl = __ENV.ACCOUNT_API_BASE_URL || 'http://account-banking-api:8082';
const coreApiBaseUrl = __ENV.CORE_API_BASE_URL || 'http://core-banking-api:8083';

export function setup() {
  const ownerId = '22222222-2222-2222-2222-222222222222';
  const createAccountResponse = http.post(`${accountApiBaseUrl}/accounts`, JSON.stringify({ ownerId }), {
    headers: { 'Content-Type': 'application/json' }
  });

  check(createAccountResponse, {
    'account created': (response) => response.status === 200 || response.status === 201
  });

  const accountId = createAccountResponse.json('id');
  const creditResponse = http.post(`${coreApiBaseUrl}/operations/credits`, JSON.stringify({
    accountId,
    money: { amount: 10000.00, currency: 'EUR' }
  }), {
    headers: {
      'Content-Type': 'application/json',
      'Idempotency-Key': `load-credit-${Date.now()}`
    }
  });

  check(creditResponse, {
    'initial credit created': (response) => response.status === 201
  });

  return { accountId };
}

export default function (data) {
  const response = http.get(`${coreApiBaseUrl}/operations/accounts/${data.accountId}?limit=10&offset=0`);
  check(response, {
    'history status is 200': (res) => res.status === 200,
    'history has items array': (res) => Array.isArray(res.json('items'))
  });
  sleep(1);
}
