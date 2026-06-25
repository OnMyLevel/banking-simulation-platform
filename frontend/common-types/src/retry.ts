export function parseRetrySeconds(value: string | null | undefined, now: Date = new Date()): number | undefined {
  if (!value) {
    return undefined;
  }

  const numericValue = Number.parseInt(value, 10);
  if (!Number.isNaN(numericValue) && numericValue >= 0) {
    return numericValue;
  }

  const dateValue = Date.parse(value);
  if (Number.isNaN(dateValue)) {
    return undefined;
  }

  const seconds = Math.ceil((dateValue - now.getTime()) / 1000);
  return seconds > 0 ? seconds : 0;
}
