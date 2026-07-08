import { Component, OnInit, inject } from '@angular/core';
import { AdvisorDashboardComponent } from './advisor-dashboard.component';
import { GatewayApiService, type AdvisorDashboardData } from './gateway-api.service';
import type { AngularGatewayErrorState } from './gateway-error.mapper';

type AppDashboardStatus = 'loading' | 'ready' | 'empty' | 'error';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [AdvisorDashboardComponent],
  template: `
    <main class="app-shell">
      @if (status === 'loading') {
        <section class="loading-card" aria-live="polite">
          <p class="eyebrow">Advisor workspace</p>
          <h1>Loading advisor dashboard</h1>
          <p class="description">Gateway data is being prepared.</p>
        </section>
      } @else {
        <app-advisor-dashboard
          [status]="status"
          [title]="dashboard?.title ?? 'Advisor workspace'"
          [items]="dashboard?.items ?? []"
          [error]="error"
        />
      }
    </main>
  `,
  styles: [
    `
      .app-shell {
        min-height: 100vh;
        display: grid;
        place-items: center;
        padding: 2rem;
        background: #f4f0ff;
        color: #211a36;
        font-family: Inter, system-ui, sans-serif;
      }

      .loading-card {
        width: min(760px, 100%);
        border-radius: 24px;
        background: #ffffff;
        padding: 2.5rem;
        box-shadow: 0 24px 70px rgb(33 26 54 / 14%);
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
    `,
  ],
})
export class AppComponent implements OnInit {
  private readonly gatewayApi = inject(GatewayApiService);

  status: AppDashboardStatus = 'loading';
  dashboard?: AdvisorDashboardData;
  error?: AngularGatewayErrorState;

  async ngOnInit(): Promise<void> {
    const result = await this.gatewayApi.loadAdvisorDashboard();

    if (result.status === 'ready') {
      this.status = 'ready';
      this.dashboard = result.data;
      this.error = undefined;
      return;
    }

    if (result.status === 'empty') {
      this.status = 'empty';
      this.dashboard = undefined;
      this.error = undefined;
      return;
    }

    this.status = 'error';
    this.dashboard = undefined;
    this.error = result.error;
  }
}
