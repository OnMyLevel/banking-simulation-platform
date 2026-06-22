#!/usr/bin/env bash
set -euo pipefail

URL="$1"
TIMEOUT_SECONDS="${2:-120}"
EXPECTED_STATUS_PREFIX="${3:-}"

end=$((SECONDS + TIMEOUT_SECONDS))
while [ "$SECONDS" -lt "$end" ]; do
  status=$(curl -s -o /dev/null -w "%{http_code}" "$URL" || true)
  if [ -n "$EXPECTED_STATUS_PREFIX" ]; then
    if [[ "$status" == "$EXPECTED_STATUS_PREFIX"* ]]; then
      echo "Ready: $URL returned $status"
      exit 0
    fi
  elif [ "$status" != "000" ]; then
    echo "Ready: $URL returned $status"
    exit 0
  fi
  sleep 2
done

echo "Timed out waiting for $URL" >&2
exit 1
