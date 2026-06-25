import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  standalone: true,
  template: `
    <main class="app-shell">
      <section class="hero-card">
        <p class="eyebrow">Banking Simulation Platform</p>
        <h1>Advisor workspace</h1>
        <p class="description">
          Angular application shell for support and operational journeys through the API Gateway.
        </p>
        <ul>
          <li>Customer support journeys</li>
          <li>Operational investigation views</li>
          <li>Gateway contract aligned access</li>
        </ul>
      </section>
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

      .hero-card {
        width: min(720px, 100%);
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

      ul {
        margin: 0;
        padding-left: 1.25rem;
        line-height: 1.8;
      }
    `,
  ],
})
export class AppComponent {}
