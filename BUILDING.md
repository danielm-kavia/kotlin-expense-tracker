# Building kotlin-expense-tracker in CI

Current state: Buildable Android Gradle project with an app module.

CI behavior:
- Run Gradle via the wrapper:
  - (cd kotlin-expense-tracker && ./gradlew tasks)
  - (cd kotlin-expense-tracker && ./gradlew assembleDebug)

Requirements:
- Java 17+
- Android SDK platform 34 available on the build agent

Troubleshooting:
- If AGP complains about Java version, ensure JAVA_HOME points to JDK 17.
- Ensure google() and mavenCentral() are reachable from CI environment.
