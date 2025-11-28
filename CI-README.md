State: Boilerplate/docs-only — Gradle project intentionally removed.

CI instructions:
- Skip Android build/test for this container while CI-NOT-READY is present.
- Do NOT run Gradle in this folder (no settings.gradle/build files are present).

To re-enable builds (future):
1) Restore minimal Gradle project:
   - settings.gradle[.kts]
   - app/build.gradle[.kts]
   - app/src/main/ with AndroidManifest and minimal sources/res
2) Remove CI-NOT-READY
3) Then you may run:
   - (cd kotlin-expense-tracker && ./gradlew tasks)
   - (cd kotlin-expense-tracker && ./gradlew assembleDebug)
