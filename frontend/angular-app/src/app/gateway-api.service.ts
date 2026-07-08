import { HttpClient, HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, catchError, map, of } from 'rxjs';
import type { FrontendApiError } from '../../../common-types/src';
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

@Injectable({ providedIn: 'root' })
export class GatewayApiService {
  private readonly advisorDashboardUrl = `${environment.gatewayBaseUrl}${environment.advisorDashboardPath}`;
  private readonly http = inject(HttpClient);

  loadAdvisorDashboard(): Observable<AdvisorDashboardResult> {
    return this.http
      .get<AdvisorDashboardData>(this.advisorDashboardUrl, {
        observe: 'response',
        headers: {
          Accept: 'application/json',
        },
      })
      .pipe(
        map((response) => this.toDashboardResult(response)),
        catchError((error: HttpErrorResponse) => of(this.toErrorResult(error))),
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

  private toErrorResult(error: HttpErrorResponse): AdvisorDashboardResult {
    return {
      status: 'error',
      error: toAngularGatewayErrorState(this.toApiError(error)),
    };
  }

  private toApiError(error: HttpErrorResponse): FrontendApiError {
    const body = this.readErrorBody(error.error);
    const fallbackMessage = 'A technical error occurred. Please try again later.';

    return {
      status: error.status,
      message: body.message ?? fallbackMessage,
      correlationId: body.correlationId ?? error.headers.get('x-correlation-id') ?? undefined,
      retryAfterSeconds: body.retryAfterSeconds ?? this.readRetryAfterSeconds(error),
    };
  }

  private readErrorBody(error: unknown): Partial<FrontendApiError> {
    return typeof error === 'object' && error !== null ? (error as Partial<FrontendApiError>) : {};
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
