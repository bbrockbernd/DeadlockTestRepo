import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration
import java.io.File

plugins {
    kotlin("jvm") version "2.1.21"
}

group = "org.example"
version = "1.0-SNAPSHOT"

val kotlinFile: String? by project
if (kotlinFile != null) { 
    sourceSets {
        getByName("main") {
            kotlin.srcDir("src/main/kotlin")
            kotlin.include(kotlinFile)
        }
    }
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.jetbrains.kotlinx:lincheck:2.40-SNAPSHOT")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
}

// Main test task configuration
tasks.test {
    useJUnitPlatform()
    // Disable running tests in the main test task
    // as we'll run them individually in separate tasks
    filter {
        excludeTestsMatching("*")
    }
}

// Find all test files in the altered package
val testDir = File("src/test/kotlin/altered")
val testFiles = testDir.listFiles { file -> 
    file.isFile && file.name.matches(Regex("test\\d+\\.kt"))
}?.sortedBy { 
    // Extract the number from the filename for proper sorting
    val numberMatch = Regex("test(\\d+)\\.kt").find(it.name)
    numberMatch?.groupValues?.get(1)?.toIntOrNull() ?: Int.MAX_VALUE
} ?: emptyList()

// Create a test task for each test file
testFiles.forEach { file ->
    val testNumber = file.nameWithoutExtension.replace("test", "")
    val taskName = "runTest$testNumber"

    // Create a test task for this specific test file
    tasks.register<Test>(taskName) {
        description = "Runs test from ${file.name}"
        group = "verification"

        // Use the same configuration as the main test task
        useJUnitPlatform()
        maxHeapSize = "32g"
        forkEvery = 1
        timeout = 60.seconds.toJavaDuration()

        // Set test to continue on failure
        ignoreFailures = true

        // Only run tests from this specific file
        filter {
            // Try both package patterns to ensure we catch the right test
            includeTestsMatching("org.example.altered.test$testNumber.RunChecker$testNumber.GPMChecker")
            includeTestsMatching("org.example.altered.RunChecker$testNumber.GPMChecker")
        }

        // Add logging to help debug test execution [AI SLOP]
        testLogging {
            events("passed", "skipped", "failed", "standardOut", "standardError")
            showExceptions = true
            showCauses = true
            showStackTraces = true
            exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        }
    }

    // Make the main test task depend on this test task
    tasks.test {
        finalizedBy(taskName)
    }
}

kotlin {
    jvmToolchain(17)
}
