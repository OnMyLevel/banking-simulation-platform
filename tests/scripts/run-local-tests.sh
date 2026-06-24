#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
COMPOSE_FILE="$ROOT_DIR/tests/docker/docker-compose.tests.yml"
RUN_LOAD="${RUN_LOAD:-true}"
RUN_SECURITY="${RUN_SECURITY:-true}"
KEEP_ENV="${KEEP_ENV:-false}"
ACCOUNT_API_WAIT_SECONDS="${ACCOUNT_API_WAIT_SECONDS:-240}"
CORE_API_WAIT_SECONDS="${CORE_API_WAIT_SECONDS:-420}"

cd "$ROOT_DIR"
mkdir -p tests/postman/results tests/security

cleanup() {
  if [ "$KEEP_ENV" != "true" ]; then
    docker compose -f "$COMPOSE_FILE" down -v --remove-orphans
  fi
}
trap cleanup EXIT

show_service_output() {
  echo "Account Banking API output:"
  docker compose -f "$COMPOSE_FILE" logs --tail=120 account-banking-api || true

  echo "Core Banking API output:"
  docker compose -f "$COMPOSE_FILE" logs --tail=120 core-banking-api || true
}

echo "Starting local Docker test environment..."
docker compose -f "$COMPOSE_FILE" up -d postgres account-banking-api core-banking-api

echo "Waiting for Account Banking API..."
if ! bash tests/scripts/wait-for-http.sh "http://localhost:8082/accounts/00000000-0000-0000-0000-000000000000" "$ACCOUNT_API_WAIT_SECONDS"; then
  show_service_output
  exit 1
fi

echo "Waiting for Core Banking API..."
if ! bash tests/scripts/wait-for-http.sh "http://localhost:8083/operations/accounts/00000000-0000-0000-0000-000000000000?limit=1&offset=0" "$CORE_API_WAIT_SECONDS" 200; then
  show_service_output
  exit 1
fi

echo "Running functional API tests with Newman..."
docker compose -f "$COMPOSE_FILE" run --rm newman

if [ "$RUN_LOAD" = "true" ]; then
  echo "Running smoke load test with k6..."
  docker compose -f "$COMPOSE_FILE" --profile load run --rm k6
else
  echo "Skipping load tests. Set RUN_LOAD=true to enable."
fi

if [ "$RUN_SECURITY" = "true" ]; then
  echo "Running baseline security scan with OWASP ZAP..."
  docker compose -f "$COMPOSE_FILE" --profile security run --rm zap || {
    echo "ZAP completed with findings. Check tests/security/zap-report.html."
  }
else
  echo "Skipping security tests. Set RUN_SECURITY=true to enable."
fi

echo "Local automated test suite completed."
