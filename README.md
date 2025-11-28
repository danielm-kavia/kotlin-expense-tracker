# kotlin-expense-tracker
Personal finance tracker app built with Kotlin that allows users to add income and expense entries, navigate between months, view transactions in a filtered table, and see a real-time balance summary with formatted currency in BRL.

Startup behavior
- The app uses an in-memory repository and does not require any backend or environment variables to launch.
- No network permissions or calls on startup.
- MainActivity renders a month label and summary cards; seed data ensures non-empty UI.

Build (Android):
- Ensure Java 17 is available.
- From repository root (CI-friendly):
  - ./gradlew tasks
  - ./gradlew assembleDebug
- Or from this folder:
  - chmod +x ./gradlew
  - ./gradlew tasks
  - ./gradlew assembleDebug
