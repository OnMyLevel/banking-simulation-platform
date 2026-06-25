<script setup lang="ts">
type DashboardStatus = 'ready' | 'empty' | 'error';

type DashboardItem = {
  label: string;
  value: string;
};

const props = withDefaults(
  defineProps<{
    status?: DashboardStatus;
    companyName?: string;
    reference?: string;
    items?: DashboardItem[];
  }>(),
  {
    status: 'ready',
    companyName: 'Company workspace',
    reference: undefined,
    items: () => [
      { label: 'Accounts', value: 'Ready' },
      { label: 'Payments', value: 'Prepared' },
      { label: 'Operations', value: 'Available' },
    ],
  },
);
</script>

<template>
  <section class="dashboard-card" :class="`dashboard-card--${props.status}`">
    <p class="eyebrow">Company dashboard</p>

    <template v-if="props.status === 'ready'">
      <h1>{{ props.companyName }}</h1>
      <p class="description">
        Business banking overview prepared for future Gateway data integration.
      </p>
      <dl class="dashboard-grid">
        <div v-for="item in props.items" :key="item.label" class="dashboard-item">
          <dt>{{ item.label }}</dt>
          <dd>{{ item.value }}</dd>
        </div>
      </dl>
    </template>

    <template v-else-if="props.status === 'empty'">
      <h1>No company data yet</h1>
      <p class="description">
        The dashboard is ready, but no company account data has been loaded yet.
      </p>
    </template>

    <template v-else>
      <h1>Company dashboard unavailable</h1>
      <p class="description">
        A technical error occurred while preparing the company dashboard.
      </p>
      <p v-if="props.reference" class="support-ref">Reference: {{ props.reference }}</p>
    </template>
  </section>
</template>

<style scoped>
.dashboard-card {
  width: min(760px, 100%);
  border-radius: 24px;
  background: #ffffff;
  padding: 2.5rem;
  box-shadow: 0 24px 70px rgb(23 32 51 / 14%);
}

.dashboard-card--error {
  border: 1px solid #fecaca;
  background: #fff7f7;
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
</style>
