# Building kotlin-expense-tracker in CI

Current state: Boilerplate/docs-only. Android Gradle project and app module were intentionally removed.

CI behavior:
- Skip building this container while the sentinel file `CI-NOT-READY` exists.
- Do NOT run Gradle here; there is no settings.gradle/build.gradle in this state.

How to re-enable builds later:
1) Recreate a minimal Android project:
   - settings.gradle[.kts] at kotlin-expense-tracker/
   - app/build.gradle[.kts]
   - app/src/main/ with AndroidManifest.xml and minimal sources/res
2) Remove `CI-NOT-READY`
3) Build examples:
   - (cd kotlin-expense-tracker && ./gradlew tasks)
   - (cd kotlin-expense-tracker && ./gradlew assembleDebug)

Troubleshooting:
- If you see "./gradlew: No such file or directory", the project is still in docs-only state or Gradle wrapper isn’t present.
- Ensure `CI-NOT-READY` exists to make CI skip builds until the app is scaffolded.
