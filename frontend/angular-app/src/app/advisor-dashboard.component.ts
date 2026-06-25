import { Component, Input } from '@angular/core';

type DashboardStatus = 'ready' | 'empty' | 'error';

type DashboardItem = {
  label: string;
  value: string;
};

@Component({
  selector: 'app-advisor-dashboard',
  standalone: true,
  template: `
    <section class="dashboard-card" [class.dashboard-card--error]="status === 'error'">
      <p class="eyebrow">Advisor workspace</p>

      @if (status === 'ready') {
        <h1>{{ title }}</h1>
        <p class="description">
          Support and operational workspace prepared for future Gateway data integration.
        </p>
        <dl class="dashboard-grid">
          @for (item of items; track item.label) {
            <div class="dashboard-item">
              <dt>{{ item.label }}</dt>
              <dd>{{ item.value }}</dd>
            </div>
          }
        </dl>
      } @else if (status === 'empty') {
        <h1>No advisor data yet</h1>
        <p class="description">
          The workspace is ready, but no support data has been loaded yet.
        </p>
      } @else {
        <h1>Advisor workspace unavailable</h1>
        <p class="description">
          A technical error occurred while preparing the advisor workspace.
        </p>
        @if (reference) {
          <p class="support-ref">Reference: {{ reference }}</p>
        }
      }
    </section>
  `,
  styles: [
    `
      .dashboard-card {
        width: min(760px, 100%);
        border-radius: 24px;
        background: #ffffff;
        padding: 2.5rem;
        box-shadow: 0 24px 70px rgb(33 26 54 / 14%);
      }

      .dashboard-card--error {
        border: 1px solid #fecaca;
        background: #fff7f7;
      }

      .eyebrow {
        margin: 0 0 0.75rem;
        color: #6c3fd1;
        font-weight: 700;
        letter-spacing: 0.08em;
        text-transform: uppercase;
      }

      h1 {
        margin: 0;
        font-size: clamp(2rem, 6vw, 4rem);
      }

      .description {
        margin: 1rem 0 1.5rem;
        font-size: 1.1rem;
        line-height: 1.6;
      }

      .dashboard-grid {
        display: grid;
        gap: 1rem;
        margin: 1.5rem 0 0;
      }

      .dashboard-item {
        padding: 1rem;
        border-radius: 1rem;
        background: #f8fafc;
      }

      dt {
        color: #64748b;
        font-size: 0.8rem;
        font-weight: 700;
        letter-spacing: 0.06em;
        text-transform: uppercase;
      }

      dd {
        margin: 0.35rem 0 0;
        color: #0f172a;
        font-size: 1.1rem;
        font-weight: 700;
      }

      .support-ref {
        margin: 1rem 0 0;
        color: #4b5563;
      }
    `,
  ],
})
export class AdvisorDashboardComponent {
  @Input() status: DashboardStatus = 'ready';
  @Input() title = 'Advisor dashboard';
  @Input() reference?: string;
  @Input() items: DashboardItem[] = [
    { label: 'Support cases', value: 'Ready' },
    { label: 'Investigations', value: 'Available' },
    { label: 'Actions', value: 'Prepared' },
  ];
}
