import { HttpClient, provideHttpClient, withInterceptors } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { CORRELATION_ID_HEADER, correlationIdInterceptor } from './correlation-id.interceptor';

describe('correlationIdInterceptor', () => {
  let http: HttpClient;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(withInterceptors([correlationIdInterceptor])), provideHttpClientTesting()],
    });

    http = TestBed.inject(HttpClient);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('adds a correlation id header when the request does not have one', () => {
    http.get('/api/advisor/dashboard').subscribe();

    const request = httpMock.expectOne('/api/advisor/dashboard');

    expect(request.request.headers.has(CORRELATION_ID_HEADER)).toBeTrue();
    expect(request.request.headers.get(CORRELATION_ID_HEADER)).toBeTruthy();

    request.flush({});
  });

  it('keeps an existing correlation id header', () => {
    http
      .get('/api/advisor/dashboard', {
        headers: {
          [CORRELATION_ID_HEADER]: 'corr-existing-123',
        },
      })
      .subscribe();

    const request = httpMock.expectOne('/api/advisor/dashboard');

    expect(request.request.headers.get(CORRELATION_ID_HEADER)).toBe('corr-existing-123');

    request.flush({});
  });
});
