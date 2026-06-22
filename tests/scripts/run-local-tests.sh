#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
COMPOSE_FILE="$ROOT_DIR/tests/docker/docker-compose.tests.yml"
RUN_LOAD="${RUN_LOAD:-true}"
RUN_SECURITY="${RUN_SECURITY:-true}"
KEEP_ENV="${KEEP_ENV:-false}"

cd "$ROOT_DIR"
mkdir -p tests/postman/results tests/security

cleanup() {
  if [ "$KEEP_ENV" != "true" ]; then
    docker compose -f "$COMPOSE_FILE" down -v --remove-orphans
  fi
}
trap cleanup EXIT

echo "Starting local Docker test environment..."
docker compose -f "$COMPOSE_FILE" up -d postgres account-banking-api core-banking-api

echo "Waiting for Account Banking API..."
bash tests/scripts/wait-for-http.sh "http://localhost:8082/accounts/00000000-0000-0000-0000-000000000000" 180

echo "Waiting for Core Banking API..."
bash tests/scripts/wait-for-http.sh "http://localhost:8083/operations/accounts/00000000-0000-0000-0000-000000000000?limit=1&offset=0" 180 200

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
