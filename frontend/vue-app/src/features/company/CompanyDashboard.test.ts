import { mount } from '@vue/test-utils';
import { describe, expect, it } from 'vitest';
import CompanyDashboard from './CompanyDashboard.vue';

describe('CompanyDashboard', () => {
  it('renders ready dashboard items', () => {
    const wrapper = mount(CompanyDashboard, {
      props: {
        companyName: 'Acme banking',
        items: [
          { label: 'Accounts', value: '3 active' },
          { label: 'Payments', value: '2 pending' },
        ],
      },
    });

    expect(wrapper.text()).toContain('Company dashboard');
    expect(wrapper.text()).toContain('Acme banking');
    expect(wrapper.text()).toContain('3 active');
    expect(wrapper.text()).toContain('2 pending');
  });

  it('renders empty state', () => {
    const wrapper = mount(CompanyDashboard, {
      props: {
        status: 'empty',
      },
    });

    expect(wrapper.text()).toContain('No company data yet');
  });

  it('renders error state with support reference', () => {
    const wrapper = mount(CompanyDashboard, {
      props: {
        status: 'error',
        reference: 'corr-company-123',
      },
    });

    expect(wrapper.text()).toContain('Company dashboard unavailable');
    expect(wrapper.text()).toContain('corr-company-123');
  });
});
