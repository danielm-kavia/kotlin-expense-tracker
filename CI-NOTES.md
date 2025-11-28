# CI Notes for kotlin-expense-tracker

The Android/Kotlin app is not yet implemented beyond a placeholder. This repository currently does not contain a complete Android project (no app module, no settings.gradle, etc.), so running `./gradlew` in CI will fail.

Why the failure occurs:
- CI attempts to run `./gradlew` from the repository root or from this folder.
- This folder includes wrapper files, but without a full Gradle project structure, `./gradlew` cannot execute meaningful tasks, and some CI environments may not mount executables in this path.

Actions for CI maintainers:
- Skip or conditionally bypass Android build/test until the Android app is scaffolded.
- Suggested guard:
  - Check for both `settings.gradle[.kts]` and an `app/` module with a `build.gradle[.kts]` before running Gradle.
  - Alternatively, look for a sentinel file such as `CI-NOT-READY` and skip the build if present.

When to enable Gradle:
- After the Android project is scaffolded (settings.gradle, app/build.gradle, source sets), re-enable Gradle tasks (e.g., `./gradlew assembleDebug` or `./gradlew test`).

Related documentation:
- See kavia-docs/expense-tracker-react-spec-for-android-parity.md for implementation requirements.
- See kavia-docs/README-android-parity.md for a summary and next steps.
