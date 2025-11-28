# kotlin-expense-tracker

Current status: Boilerplate/docs-only
- The previous Android app implementation and Gradle project were removed to reset to a clean starting point.
- CI sentinel `CI-NOT-READY` is present; CI should skip Android build steps for this container.

Next steps for implementation:
- Recreate Gradle project (settings.gradle[.kts], app/build.gradle[.kts], and minimal app/src).
- Remove `CI-NOT-READY` to re-enable builds.
- Then you can run:
  - (cd kotlin-expense-tracker && ./gradlew tasks)
  - (cd kotlin-expense-tracker && ./gradlew assembleDebug)
