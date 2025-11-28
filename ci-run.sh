#!/usr/bin/env bash
# Helper for CI. When CI-NOT-READY is present (docs-only state), do nothing and exit 0.
set -euo pipefail

if [ -f "./CI-NOT-READY" ]; then
  echo "[ci-run] kotlin-expense-tracker is in docs-only/boilerplate state. Skipping Gradle."
  exit 0
fi

if [ -f "./gradlew" ]; then
  chmod +x ./gradlew || true
  exec ./gradlew "$@"
else
  echo "[ci-run] Gradle wrapper not found and project not ready. If this is intentional, create CI-NOT-READY to skip."
  exit 0
fi
