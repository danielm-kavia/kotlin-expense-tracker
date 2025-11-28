# Building kotlin-expense-tracker in CI

Preferred (from repo root):
- bash ./gradlew-kotlin-expense-tracker.sh tasks
- bash ./gradlew-kotlin-expense-tracker.sh assembleDebug

Alternative (from module folder):
- cd kotlin-expense-tracker
- chmod +x ./gradlew
- ./gradlew tasks
- ./gradlew assembleDebug

If you see "./gradlew: No such file or directory", ensure you are inside the kotlin-expense-tracker directory or use the helper script above.
