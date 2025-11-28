# Kotlin Expense Tracker (Android) - Boilerplate/Docs-only

State:
- This folder is intentionally cleaned to a boilerplate/docs-only state.
- No Gradle settings or app module are present.
- A sentinel file `CI-NOT-READY` is included so CI skips Android builds.

CI/Local builds:
- Do NOT run Gradle here until the project is scaffolded again.
- CI should check for `CI-NOT-READY` and skip.

When you are ready to implement:
1) Add minimal Gradle setup:
   - settings.gradle[.kts] at project root
   - app/build.gradle[.kts]
   - app/src/main/ with AndroidManifest.xml and minimal sources/res
2) Remove `CI-NOT-READY`
3) Then build from this folder:
   - ./gradlew tasks
   - ./gradlew assembleDebug

Requirements for future builds:
- Java 17 for AGP 8.x+.
- Repositories: google() and mavenCentral() in settings/dependencyResolution.
