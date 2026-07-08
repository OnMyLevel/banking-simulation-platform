import { HttpClient, provideHttpClient, withInterceptors } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { AUTHORIZATION_HEADER, authInterceptor } from './auth.interceptor';
import { AuthTokenService } from './auth-token.service';
import { CORRELATION_ID_HEADER, correlationIdInterceptor } from './correlation-id.interceptor';

describe('authInterceptor', () => {
  let http: HttpClient;
  let httpMock: HttpTestingController;
  let tokenService: AuthTokenService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(withInterceptors([correlationIdInterceptor, authInterceptor])), provideHttpClientTesting()],
    });

    http = TestBed.inject(HttpClient);
    httpMock = TestBed.inject(HttpTestingController);
    tokenService = TestBed.inject(AuthTokenService);
  });

  afterEach(() => {
    httpMock.verify();
    tokenService.clearToken();
  });

  it('adds authorization header when a token is available', () => {
    tokenService.setToken('jwt-token');

    http.get('/api/advisor/dashboard').subscribe();

    const request = httpMock.expectOne('/api/advisor/dashboard');
    expect(request.request.headers.get(AUTHORIZATION_HEADER)).toBe('Bearer jwt-token');

    request.flush({});
  });

  it('does not add authorization header when no token is available', () => {
    http.get('/api/advisor/dashboard').subscribe();

    const request = httpMock.expectOne('/api/advisor/dashboard');
    expect(request.request.headers.has(AUTHORIZATION_HEADER)).toBeFalse();

    request.flush({});
  });

  it('keeps an existing authorization header', () => {
    tokenService.setToken('jwt-token');

    http
      .get('/api/advisor/dashboard', {
        headers: {
          [AUTHORIZATION_HEADER]: 'Bearer existing-token',
        },
      })
      .subscribe();

    const request = httpMock.expectOne('/api/advisor/dashboard');
    expect(request.request.headers.get(AUTHORIZATION_HEADER)).toBe('Bearer existing-token');

    request.flush({});
  });

  it('works with the correlation id interceptor', () => {
    tokenService.setToken('jwt-token');

    http.get('/api/advisor/dashboard').subscribe();

    const request = httpMock.expectOne('/api/advisor/dashboard');
    expect(request.request.headers.get(AUTHORIZATION_HEADER)).toBe('Bearer jwt-token');
    expect(request.request.headers.has(CORRELATION_ID_HEADER)).toBeTrue();

    request.flush({});
  });
});
