import { describe, expect, it } from 'vitest';
import { parseRetrySeconds } from './retry';

describe('parseRetrySeconds', () => {
  it('returns numeric values', () => {
    expect(parseRetrySeconds('30')).toBe(30);
  });

  it('returns undefined for missing values', () => {
    expect(parseRetrySeconds(null)).toBeUndefined();
    expect(parseRetrySeconds(undefined)).toBeUndefined();
  });

  it('returns undefined for invalid values', () => {
    expect(parseRetrySeconds('not-a-date')).toBeUndefined();
  });

  it('supports date values', () => {
    const now = new Date('2026-06-25T10:00:00.000Z');

    expect(parseRetrySeconds('Thu, 25 Jun 2026 10:00:30 GMT', now)).toBe(30);
  });
});
