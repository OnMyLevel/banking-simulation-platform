import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AdvisorDashboardComponent } from './advisor-dashboard.component';

describe('AdvisorDashboardComponent', () => {
  let fixture: ComponentFixture<AdvisorDashboardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdvisorDashboardComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(AdvisorDashboardComponent);
  });

  it('renders ready items', () => {
    fixture.componentRef.setInput('title', 'Advisor operations');
    fixture.componentRef.setInput('items', [
      { label: 'Support cases', value: '4 open' },
      { label: 'Reviews', value: '2 pending' },
    ]);
    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.textContent).toContain('Advisor operations');
    expect(compiled.textContent).toContain('4 open');
    expect(compiled.textContent).toContain('2 pending');
  });

  it('renders empty state', () => {
    fixture.componentRef.setInput('status', 'empty');
    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.textContent).toContain('No advisor data yet');
  });

  it('renders mapped error state with reference and retry delay', () => {
    fixture.componentRef.setInput('status', 'error');
    fixture.componentRef.setInput('error', {
      title: 'Too many requests',
      message: 'Too many requests. Please try again later.',
      reference: 'corr-advisor-123',
      retryAfterSeconds: 30,
    });
    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.textContent).toContain('Too many requests');
    expect(compiled.textContent).toContain('Retry after 30 seconds.');
    expect(compiled.textContent).toContain('corr-advisor-123');
  });
});
