<script setup lang="ts">
import CompanyDashboard from './features/company/CompanyDashboard.vue';
</script>

<template>
  <main class="app-shell">
    <CompanyDashboard company-name="Company banking portal" />
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
</style>
