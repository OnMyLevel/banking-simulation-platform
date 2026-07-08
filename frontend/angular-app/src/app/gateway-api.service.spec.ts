import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { fakeAsync, TestBed, tick } from '@angular/core/testing';
import { GatewayApiService } from './gateway-api.service';

describe('GatewayApiService', () => {
  let service: GatewayApiService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });

    service = TestBed.inject(GatewayApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('calls the configured advisor dashboard endpoint', () => {
    service.loadAdvisorDashboard().subscribe();

    const request = httpMock.expectOne('/api/advisor/dashboard');
    expect(request.request.method).toBe('GET');
    request.flush({
      title: 'Advisor operations',
      items: [{ label: 'Support cases', value: '4 open' }],
    });
  });

  it('loads advisor dashboard data', () => {
    service.loadAdvisorDashboard().subscribe((result) => {
      expect(result.status).toBe('ready');
      if (result.status === 'ready') {
        expect(result.data.title).toBe('Advisor operations');
        expect(result.data.items[0].value).toBe('4 open');
      }
    });

    const request = httpMock.expectOne('/api/advisor/dashboard');
    request.flush({
      title: 'Advisor operations',
      items: [{ label: 'Support cases', value: '4 open' }],
    });
  });

  it('maps empty responses', () => {
    service.loadAdvisorDashboard().subscribe((result) => {
      expect(result.status).toBe('empty');
    });

    const request = httpMock.expectOne('/api/advisor/dashboard');
    request.flush(null, { status: 204, statusText: 'No Content' });
  });

  it('maps empty item responses', () => {
    service.loadAdvisorDashboard().subscribe((result) => {
      expect(result.status).toBe('empty');
    });

    const request = httpMock.expectOne('/api/advisor/dashboard');
    request.flush({
      title: 'Advisor operations',
      items: [],
    });
  });

  it('does not retry client errors', () => {
    service.loadAdvisorDashboard().subscribe((result) => {
      expect(result.status).toBe('error');
      if (result.status === 'error') {
        expect(result.error.title).toBe('Too many requests');
        expect(result.error.reference).toBe('corr-angular-429');
        expect(result.error.retryAfterSeconds).toBe(30);
      }
    });

    const request = httpMock.expectOne('/api/advisor/dashboard');
    request.flush(
      {
        status: 429,
        message: 'Too many requests. Please try again later.',
        correlationId: 'corr-angular-429',
      },
      {
        status: 429,
        statusText: 'Too Many Requests',
        headers: { 'retry-after': '30' },
      },
    );

    httpMock.expectNone('/api/advisor/dashboard');
  });

  it('retries temporary gateway errors and returns the successful retry result', fakeAsync(() => {
    service.loadAdvisorDashboard().subscribe((result) => {
      expect(result.status).toBe('ready');
      if (result.status === 'ready') {
        expect(result.data.items[0].value).toBe('4 open');
      }
    });

    const firstRequest = httpMock.expectOne('/api/advisor/dashboard');
    firstRequest.flush(null, { status: 503, statusText: 'Service Unavailable' });

    tick(100);

    const retryRequest = httpMock.expectOne('/api/advisor/dashboard');
    retryRequest.flush({
      title: 'Advisor operations',
      items: [{ label: 'Support cases', value: '4 open' }],
    });
  }));

  it('stops retrying temporary gateway errors after the retry budget is exhausted', fakeAsync(() => {
    service.loadAdvisorDashboard().subscribe((result) => {
      expect(result.status).toBe('error');
      if (result.status === 'error') {
        expect(result.error.title).toBe('Technical error');
      }
    });

    const firstRequest = httpMock.expectOne('/api/advisor/dashboard');
    firstRequest.flush(null, { status: 503, statusText: 'Service Unavailable' });

    tick(100);

    const secondRequest = httpMock.expectOne('/api/advisor/dashboard');
    secondRequest.flush(null, { status: 503, statusText: 'Service Unavailable' });

    tick(100);

    const thirdRequest = httpMock.expectOne('/api/advisor/dashboard');
    thirdRequest.flush(null, { status: 503, statusText: 'Service Unavailable' });
  }));

  it('maps technical errors when no JSON body is available', fakeAsync(() => {
    service.loadAdvisorDashboard().subscribe((result) => {
      expect(result.status).toBe('error');
      if (result.status === 'error') {
        expect(result.error.title).toBe('Technical error');
        expect(result.error.message).toBe('A technical error occurred. Please try again later.');
      }
    });

    const firstRequest = httpMock.expectOne('/api/advisor/dashboard');
    firstRequest.error(new ProgressEvent('error'), { status: 500, statusText: 'Server Error' });
  }));
});
