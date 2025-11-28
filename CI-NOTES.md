# CI Notes for kotlin-expense-tracker

State: Boilerplate/docs-only. All app implementation, module configs, manifests, and resources have been intentionally removed.

CI behavior:
- Do NOT attempt to run Gradle here. This folder is intentionally non-buildable.
- Use a sentinel file `CI-NOT-READY` to skip build steps for this container.

Re-enabling builds:
- When starting implementation, add back a minimal Gradle setup:
  - settings.gradle[.kts] with module includes
  - app/build.gradle[.kts]
  - app/src/main/AndroidManifest.xml and minimal sources/resources
- After scaffolding, CI can run: (cd kotlin-expense-tracker && ./gradlew assembleDebug)

Guards for CI:
- Only run Android build if BOTH files exist:
  - kotlin-expense-tracker/settings.gradle[.kts]
  - kotlin-expense-tracker/app/build.gradle[.kts]
- Or if `CI-NOT-READY` is ABSENT.

Related documentation:
- See kavia-docs/expense-tracker-react-spec-for-android-parity.md for implementation requirements.
- See kavia-docs/README-android-parity.md for a summary and next steps.
