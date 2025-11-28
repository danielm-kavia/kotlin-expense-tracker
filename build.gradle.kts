import org.gradle.api.JavaVersion

plugins {
    // Managed by settings pluginManagement; versions declared here for clarity
    id("com.android.application") version "8.6.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.24" apply false
}

allprojects {
    // CI-safe: avoid parallel issues on limited containers
    tasks.withType<JavaCompile>().configureEach {
        // ensure consistent encoding
        options.encoding = "UTF-8"
    }
}

tasks.register("ciInfo") {
    group = "verification"
    description = "Prints basic CI environment info."
    doLast {
        println("kotlin-expense-tracker minimal Android project is initialized.")
    }
}
