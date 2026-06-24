#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
COMPOSE_FILE="$ROOT_DIR/tests/docker/docker-compose.tests.yml"
DESTINATIONS="${DESTINATIONS:-OBSERVABILITY_HTTP FLUENT_BIT KAFKA NOOP}"
KEEP_ENV="${KEEP_ENV:-false}"

cd "$ROOT_DIR"

cleanup() {
  if [ "$KEEP_ENV" != "true" ]; then
    docker compose -f "$COMPOSE_FILE" --profile outbox down -v --remove-orphans
  fi
}
trap cleanup EXIT

json_value() {
  python3 -c 'import json,sys; print(json.load(sys.stdin)[sys.argv[1]])' "$1"
}

new_uuid() {
  python3 -c 'import uuid; print(uuid.uuid4())'
}

wait_for_http() {
  local url="$1"
  local expected_status="${2:-200}"
  local timeout="${3:-240}"
  local elapsed=0
  local status=""
  until [ "$elapsed" -ge "$timeout" ]; do
    status=$(curl -s -o /dev/null -w "%{http_code}" "$url" || true)
    if [ "$status" = "$expected_status" ]; then
      return 0
    fi
    sleep 2
    elapsed=$((elapsed + 2))
  done
  echo "Timeout waiting for $url with status $expected_status. Last status: ${status:-none}"
  return 1
}

psql_value() {
  docker compose -f "$COMPOSE_FILE" --profile outbox exec -T postgres psql -U banking -d banking_simulation -tAc "$1" | tr -d '[:space:]'
}

wait_for_outbox_status() {
  local event_key="$1"
  local destination="$2"
  local expected_status="$3"
  local timeout="${4:-90}"
  local elapsed=0
  local result=""

  until [ "$elapsed" -ge "$timeout" ]; do
    result=$(psql_value "SELECT destination_type || ':' || status FROM core_schema.outbox_events WHERE payload LIKE '%${event_key}%' ORDER BY created_at DESC LIMIT 1;")
    if [ "$result" = "${destination}:${expected_status}" ]; then
      return 0
    fi
    sleep 3
    elapsed=$((elapsed + 3))
  done

  echo "Outbox event did not reach ${destination}:${expected_status}. Last value: ${result:-empty}"
  return 1
}

run_destination_check() {
  local destination="$1"
  local owner_id
  local account_response
  local account_id
  local event_key

  echo "--- Checking outbox destination: $destination"
  BANKING_OUTBOX_DESTINATION_TYPE="$destination" docker compose -f "$COMPOSE_FILE" --profile outbox up -d --force-recreate core-banking-api
  wait_for_http "http://localhost:8083/operations/accounts/00000000-0000-0000-0000-000000000000?limit=1&offset=0" 200 240

  owner_id="$(new_uuid)"
  account_response=$(curl -s -X POST "http://localhost:8082/accounts" \
    -H "Content-Type: application/json" \
    -d "{\"ownerId\":\"${owner_id}\",\"currency\":\"EUR\"}")
  account_id=$(printf '%s' "$account_response" | json_value id)
  event_key="outbox-${destination}-$(date +%s)-$RANDOM"

  curl -s -f -X POST "http://localhost:8083/operations/credits" \
    -H "Content-Type: application/json" \
    -H "Idempotency-Key: ${event_key}" \
    -d "{\"accountId\":\"${account_id}\",\"money\":{\"amount\":1.00,\"currency\":\"EUR\"}}" >/dev/null

  wait_for_outbox_status "$event_key" "$destination" "SENT" 90
  echo "OK: $destination delivery reached SENT"
}

echo "Starting outbox delivery test dependencies..."
docker compose -f "$COMPOSE_FILE" --profile outbox up -d postgres kafka fluent-bit account-banking-api observability-api

wait_for_http "http://localhost:8082/accounts/00000000-0000-0000-0000-000000000000" 404 240

for destination in $DESTINATIONS; do
  run_destination_check "$destination"
done

echo "Outbox delivery checks completed."
