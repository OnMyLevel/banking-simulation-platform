<script setup lang="ts">
const highlights = [
  'Company account consultation',
  'Payment preparation',
  'Operation history',
];
</script>

<template>
  <main class="app-shell">
    <section class="hero-card">
      <p class="eyebrow">Banking Simulation Platform</p>
      <h1>Company banking portal</h1>
      <p class="description">
        Vue application shell for company customer journeys through the API Gateway.
      </p>
      <ul>
        <li v-for="highlight in highlights" :key="highlight">
          {{ highlight }}
        </li>
      </ul>
    </section>
  </main>
</template>

<style scoped>
.app-shell {
  min-height: 100vh;
  display: grid;
  place-items: center;
  padding: 2rem;
  background: #eef4ff;
  color: #172033;
  font-family: Inter, system-ui, sans-serif;
}

.hero-card {
  width: min(720px, 100%);
  border-radius: 24px;
  background: #ffffff;
  padding: 2.5rem;
  box-shadow: 0 24px 70px rgb(23 32 51 / 14%);
}

.eyebrow {
  margin: 0 0 0.75rem;
  color: #3366cc;
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
</style>
