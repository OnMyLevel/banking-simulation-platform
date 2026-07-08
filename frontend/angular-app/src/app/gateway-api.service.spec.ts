import { HttpErrorResponse } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
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

  it('maps gateway errors through the shared error mapper', () => {
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
  });

  it('maps technical errors when no JSON body is available', () => {
    service.loadAdvisorDashboard().subscribe((result) => {
      expect(result.status).toBe('error');
      if (result.status === 'error') {
        expect(result.error.title).toBe('Technical error');
        expect(result.error.message).toBe('A technical error occurred. Please try again later.');
      }
    });

    const request = httpMock.expectOne('/api/advisor/dashboard');
    request.error(new ProgressEvent('error'), { status: 500, statusText: 'Server Error' });
  });
});
