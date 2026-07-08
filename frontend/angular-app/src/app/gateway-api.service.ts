import { HttpClient, HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import type { FrontendApiError } from '../../../common-types/src';
import { catchError, map, of, retry, throwError, timer, timeout, type Observable } from 'rxjs';
import { environment } from '../environments/environment';
import { toAngularGatewayErrorState, type AngularGatewayErrorState } from './gateway-error.mapper';

export type AdvisorDashboardData = {
  title: string;
  items: Array<{
    label: string;
    value: string;
  }>;
};

export type AdvisorDashboardResult =
  | {
      status: 'ready';
      data: AdvisorDashboardData;
    }
  | {
      status: 'empty';
    }
  | {
      status: 'error';
      error: AngularGatewayErrorState;
    };

const GATEWAY_TIMEOUT_MS = 5000;
const GATEWAY_RETRY_COUNT = 2;
const GATEWAY_RETRY_DELAY_MS = 100;

@Injectable({ providedIn: 'root' })
export class GatewayApiService {
  private readonly advisorDashboardUrl = `${environment.gatewayBaseUrl}${environment.advisorDashboardPath}`;
  private readonly http = inject(HttpClient);

  loadAdvisorDashboard(): Observable<AdvisorDashboardResult> {
    return this.http
      .get<AdvisorDashboardData>(this.advisorDashboardUrl, {
        observe: 'response',
      })
      .pipe(
        timeout(GATEWAY_TIMEOUT_MS),
        retry({
          count: GATEWAY_RETRY_COUNT,
          delay: (error: unknown) => (this.shouldRetry(error) ? timer(GATEWAY_RETRY_DELAY_MS) : throwError(() => error)),
        }),
        map((response) => this.toDashboardResult(response)),
        catchError((error: unknown) => of(this.toErrorResult(this.toHttpError(error)))),
      );
  }

  private toDashboardResult(response: HttpResponse<AdvisorDashboardData>): AdvisorDashboardResult {
    if (response.status === 204 || !response.body || response.body.items.length === 0) {
      return { status: 'empty' };
    }

    return {
      status: 'ready',
      data: response.body,
    };
  }

  private shouldRetry(error: unknown): boolean {
    if (!(error instanceof HttpErrorResponse)) {
      return false;
    }

    return error.status === 0 || error.status === 502 || error.status === 503 || error.status === 504;
  }

  private toErrorResult(error: HttpErrorResponse): AdvisorDashboardResult {
    return {
      status: 'error',
      error: toAngularGatewayErrorState(this.toApiError(error)),
    };
  }

  private toHttpError(error: unknown): HttpErrorResponse {
    if (error instanceof HttpErrorResponse) {
      return error;
    }

    return new HttpErrorResponse({
      error,
      status: 0,
      statusText: 'Gateway timeout',
    });
  }

  private toApiError(error: HttpErrorResponse): FrontendApiError {
    const body = this.readErrorBody(error.error);

    return {
      status: error.status,
      message: body.message ?? 'A technical error occurred. Please try again later.',
      correlationId: body.correlationId ?? error.headers.get('x-correlation-id') ?? undefined,
      retryAfterSeconds: body.retryAfterSeconds ?? this.readRetryAfterSeconds(error),
    };
  }

  private readErrorBody(errorBody: unknown): Partial<FrontendApiError> {
    if (!errorBody || typeof errorBody !== 'object') {
      return {};
    }

    return errorBody as Partial<FrontendApiError>;
  }

  private readRetryAfterSeconds(error: HttpErrorResponse): number | undefined {
    const retryAfter = error.headers.get('retry-after');

    if (!retryAfter) {
      return undefined;
    }

    const parsedRetryAfter = Number(retryAfter);
    return Number.isFinite(parsedRetryAfter) ? parsedRetryAfter : undefined;
  }
}
