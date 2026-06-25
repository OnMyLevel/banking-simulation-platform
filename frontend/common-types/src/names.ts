export const CLIENT_FIELD_NAMES = {
  access: 'Authorization',
  trace: 'X-Correlation-Id',
  retry: 'Retry-After',
} as const;

export type ClientFieldName = (typeof CLIENT_FIELD_NAMES)[keyof typeof CLIENT_FIELD_NAMES];
