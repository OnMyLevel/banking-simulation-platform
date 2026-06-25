import { Component } from '@angular/core';
import { AdvisorDashboardComponent } from './advisor-dashboard.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [AdvisorDashboardComponent],
  template: `
    <main class="app-shell">
      <app-advisor-dashboard title="Advisor workspace" />
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
    `,
  ],
})
export class AppComponent {}
