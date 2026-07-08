import { TestBed } from '@angular/core/testing';
import { AppComponent } from './app.component';
import { GatewayApiService, type AdvisorDashboardResult } from './gateway-api.service';

describe('AppComponent', () => {
  it('renders loading state before Gateway data is resolved', () => {
    TestBed.configureTestingModule({
      imports: [AppComponent],
      providers: [{ provide: GatewayApiService, useValue: gatewayService(pendingResult()) }],
    });

    const fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.textContent).toContain('Loading advisor dashboard');
  });

  it('renders ready Gateway data', async () => {
    TestBed.configureTestingModule({
      imports: [AppComponent],
      providers: [
        {
          provide: GatewayApiService,
          useValue: gatewayService(
            Promise.resolve({
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
    await fixture.whenStable();
    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.textContent).toContain('Advisor operations');
    expect(compiled.textContent).toContain('4 open');
  });

  it('renders empty Gateway state', async () => {
    TestBed.configureTestingModule({
      imports: [AppComponent],
      providers: [{ provide: GatewayApiService, useValue: gatewayService(Promise.resolve({ status: 'empty' })) }],
    });

    const fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();
    await fixture.whenStable();
    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.textContent).toContain('No advisor data yet');
  });

  it('renders mapped Gateway errors', async () => {
    TestBed.configureTestingModule({
      imports: [AppComponent],
      providers: [
        {
          provide: GatewayApiService,
          useValue: gatewayService(
            Promise.resolve({
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
    await fixture.whenStable();
    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.textContent).toContain('Technical error');
    expect(compiled.textContent).toContain('corr-app-500');
  });
});

function gatewayService(result: Promise<AdvisorDashboardResult>): Pick<GatewayApiService, 'loadAdvisorDashboard'> {
  return {
    loadAdvisorDashboard: () => result,
  };
}

function pendingResult(): Promise<AdvisorDashboardResult> {
  return new Promise(() => undefined);
}
