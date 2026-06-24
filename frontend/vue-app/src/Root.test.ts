import { mount } from '@vue/test-utils';
import { describe, expect, it } from 'vitest';
import Root from './Root.vue';

describe('Root', () => {
  it('renders company banking title', () => {
    const wrapper = mount(Root);

    expect(wrapper.text()).toContain('Company banking portal');
  });
});
