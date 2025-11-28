CI usage (docs-only state):
- This container is in a boilerplate/docs-only state. Do NOT attempt to run Gradle.
- Presence of CI-NOT-READY indicates that builds should be skipped.

Recommended CI guard:
- Skip Android build if any of the following is true:
  - File kotlin-expense-tracker/CI-NOT-READY exists
  - File kotlin-expense-tracker/settings.gradle[.kts] is missing
  - File kotlin-expense-tracker/app/build.gradle[.kts] is missing

When ready to re-enable:
- Restore minimal Gradle project and remove CI-NOT-READY, then:
  - (cd kotlin-expense-tracker && ./gradlew tasks)
  - (cd kotlin-expense-tracker && ./gradlew assembleDebug)

Troubleshooting:
- If you see "./gradlew: No such file or directory", the project is still in docs-only state or wrapper is absent.
- Ensure CI checks for CI-NOT-READY and presence of Gradle settings before invoking Gradle.
