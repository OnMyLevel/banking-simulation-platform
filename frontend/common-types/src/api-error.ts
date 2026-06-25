export type FrontendApiError = {
  status: number;
  message: string;
  code?: string;
  correlationId?: string;
  retryAfterSeconds?: number;
};

export type FrontendApiMeta = {
  correlationId?: string;
};

export type FrontendApiResult<T> = {
  data: T;
  meta?: FrontendApiMeta;
};
