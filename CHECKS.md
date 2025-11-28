CI usage:
- Ensure gradle wrapper is executed from inside kotlin-expense-tracker:
  cd kotlin-expense-tracker && ./gradlew tasks
- If running from monorepo root, use:
  (cd kotlin-expense-tracker && ./gradlew tasks)

Troubleshooting:
- If you see "./gradlew: No such file or directory", verify the working directory and that the file is executable (chmod +x kotlin-expense-tracker/gradlew).
