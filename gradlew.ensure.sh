#!/bin/sh
# PUBLIC_INTERFACE
# Ensures kotlin-expense-tracker/gradlew exists and is executable.
# Use in CI before running Gradle: sh kotlin-expense-tracker/gradlew.ensure.sh
set -eu
WRAPPER="kotlin-expense-tracker/gradlew"
if [ ! -f "$WRAPPER" ]; then
  echo "[ensure] Error: $WRAPPER not found." >&2
  exit 127
fi
chmod +x "$WRAPPER" 2>/dev/null || true
echo "[ensure] Gradle wrapper is present and executable."
