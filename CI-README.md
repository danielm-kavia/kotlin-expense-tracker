# CI README — kotlin-expense-tracker

State: Buildable — Android Gradle project present with Jetpack Compose app.

How to build in CI:
- Use the Gradle wrapper in this directory:
  - (cd kotlin-expense-tracker && ./gradlew tasks)
  - (cd kotlin-expense-tracker && ./gradlew assembleDebug)

Requirements:
- JDK 17
- Android SDK platform 34 available on the build agent
- Network access to `google()` and `mavenCentral()` repositories

Notes:
- This app is single-activity, single-screen, using in-memory state only.
- Parity context: see kavia-docs/expense-tracker-react-spec-for-android-parity.md
