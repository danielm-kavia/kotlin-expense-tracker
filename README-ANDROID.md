# Kotlin Expense Tracker (Android) - Minimal Scaffold

This is a minimal Android/Kotlin project scaffold to allow CI builds to succeed.

How to build locally or in CI:
- Linux/macOS:
  ./gradlew tasks
  ./gradlew assembleDebug

Notes:
- Uses Gradle wrapper included in this repo.
- Requires Java 17 for Android Gradle Plugin 8.x.
- No external SDK manager steps are needed; Gradle will resolve dependencies from google() and mavenCentral().

Module structure:
- settings.gradle.kts includes ':app'
- app module contains a minimal MainActivity with a simple TextView UI.
