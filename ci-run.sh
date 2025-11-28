#!/usr/bin/env bash
# Helper to run Gradle reliably in CI from repository root or this folder.
set -euo pipefail

if [ -f "./gradlew" ]; then
  chmod +x ./gradlew || true
  exec ./gradlew "$@"
else
  echo "Gradle wrapper not found in current directory: $(pwd)"
  echo "Hint: cd kotlin-expense-tracker && ./gradlew tasks"
  exit 127
fi
