#!/usr/bin/env bash
# PUBLIC_INTERFACE
# Sentinel-aware Gradle entrypoint for CI.
# Usage (from repo root or CI):
#   bash ./gradlew-kotlin-expense-tracker.sh <gradle-args>
# Behavior:
# - If kotlin-expense-tracker/CI-NOT-READY exists, this script exits 0 (skip build).
# - If gradlew is missing, exit 0 with a helpful message (docs-only state).
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
APP_DIR="${ROOT_DIR}/kotlin-expense-tracker"

if [ -f "${APP_DIR}/CI-NOT-READY" ]; then
  echo "[gradlew-kotlin-expense-tracker] CI-NOT-READY present; skipping Android build."
  exit 0
fi

if [ ! -f "${APP_DIR}/gradlew" ]; then
  echo "[gradlew-kotlin-expense-tracker] Gradle wrapper missing. Project likely in docs-only state. Skipping."
  exit 0
fi

cd "${APP_DIR}"
chmod +x ./gradlew || true
exec ./gradlew "$@"
