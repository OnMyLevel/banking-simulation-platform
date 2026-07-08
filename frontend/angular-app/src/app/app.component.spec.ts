import { TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { AppComponent } from './app.component';
import { GatewayApiService, type AdvisorDashboardResult } from './gateway-api.service';

describe('AppComponent', () => {
  it('renders loading state before Gateway data is resolved', () => {
    TestBed.configureTestingModule({
      imports: [AppComponent],
      providers: [{ provide: GatewayApiService, useValue: gatewayService(new Observable<AdvisorDashboardResult>()) }],
    });

    const fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.textContent).toContain('Loading advisor dashboard');
  });

  it('renders ready Gateway data', () => {
    TestBed.configureTestingModule({
      imports: [AppComponent],
      providers: [
        {
          provide: GatewayApiService,
          useValue: gatewayService(
            of({
              status: 'ready',
              data: {
                title: 'Advisor operations',
                items: [{ label: 'Support cases', value: '4 open' }],
              },
            }),
          ),
        },
      ],
    });

    const fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.textContent).toContain('Advisor operations');
    expect(compiled.textContent).toContain('4 open');
  });

  it('renders empty Gateway state', () => {
    TestBed.configureTestingModule({
      imports: [AppComponent],
      providers: [{ provide: GatewayApiService, useValue: gatewayService(of({ status: 'empty' })) }],
    });

    const fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.textContent).toContain('No advisor data yet');
  });

  it('renders mapped Gateway errors', () => {
    TestBed.configureTestingModule({
      imports: [AppComponent],
      providers: [
        {
          provide: GatewayApiService,
          useValue: gatewayService(
            of({
              status: 'error',
              error: {
                title: 'Technical error',
                message: 'A technical error occurred. Please try again later.',
                reference: 'corr-app-500',
              },
            }),
          ),
        },
      ],
    });

    const fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.textContent).toContain('Technical error');
    expect(compiled.textContent).toContain('corr-app-500');
  });
});

function gatewayService(result: Observable<AdvisorDashboardResult>): Pick<GatewayApiService, 'loadAdvisorDashboard'> {
  return {
    loadAdvisorDashboard: () => result,
  };
}
