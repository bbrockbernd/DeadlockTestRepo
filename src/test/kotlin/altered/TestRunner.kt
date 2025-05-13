package altered

import org.example.altered.RunCheckerBase
import org.junit.jupiter.api.Test
import java.io.File
import java.io.FileWriter
import java.lang.Thread.sleep
import java.util.concurrent.Executors
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeSource

class TestRunner {
    
    @Test
    fun myTest() {
        val result = executeSingleTest(50)
        println(result)
        val results = listOf(result, result)
        FileWriter("test_runner_results.csv").use { writer ->
            writer.write("TestNumber,Status,Duration,Message\n")
            results.forEach { result ->
                writer.write(
                    "${result.testNumber},${result.status.name},${result.duration.inWholeSeconds},\"${result.msg.replace("\"", "")}\"\n"
                )
            }
        }
    }
    
    @Test
    fun collectTestResults() {
        val testClasses = findRunCheckerClasses()
        println("Found ${testClasses.size} test classes")
        
        val executor = Executors.newFixedThreadPool(8)
        val futures = testClasses
            .filter { it.key < 100 }
            .map { entry ->
            executor.submit<TestResult> { 
                val a = executeSingleTest(entry.key) 
                println("Test ${entry.key}: ${a.status.name}")
                a
            }
        }
        
        val results = futures.map { it.get() }
        
        println(results)

        FileWriter("test_runner_results.csv").use { writer ->
            writer.write("TestNumber,Status,Duration,Message\n")
            results.forEach { result ->
                writer.write(
                    "${result.testNumber},${result.status.name},${result.duration.inWholeSeconds},\"${result.msg.replace("\"", "")}\"\n"
                )
            }
        }
        executor.shutdown()
    }
    
}

private fun executeSingleTest(testNr: Int): TestResult {
    val mark = TimeSource.Monotonic.markNow()
    val processBuilder = ProcessBuilder(
        "java",
        "-cp",
        System.getProperty("java.class.path"),
        "altered.SingleTestExecutor",
        testNr.toString()
    )
    
    processBuilder.redirectErrorStream(true)
    val process = processBuilder.start()
    
    val hook = Thread {
        // Clean up child processes here
        if (process.isAlive) {
            process.destroyForcibly()
        }
    }
    Runtime.getRuntime().addShutdownHook(hook)

    try {
        val output = process.inputStream.bufferedReader().use { it.readText() }

        while (process.isAlive && mark.elapsedNow() < 30.seconds) {
            sleep(100)
        }

        if (process.isAlive) {
            process.destroyForcibly()
            return TestResult(testNr, TestResult.Status.TIMEOUT, mark.elapsedNow(), "")
        }


        val relevant = output.lines().let { lines ->
            val doneIndex = lines.indexOfFirst { it.contains("DONE") }
            if (doneIndex >= 0) {
                lines.drop(doneIndex + 1).joinToString("\n")
            } else {
                ""
            }
        }

        val first = relevant.lines()[0]
        val message = relevant.lines().drop(1).joinToString("\n")

        return when {
            first.contains(TestResult.Status.SUCCESS.name) -> TestResult(testNr, TestResult.Status.SUCCESS, mark.elapsedNow(), "")
            first.contains(TestResult.Status.DEADLOCK.name) -> TestResult(testNr, TestResult.Status.DEADLOCK, mark.elapsedNow(), message)
            first.contains(TestResult.Status.ERROR.name) -> TestResult(testNr, TestResult.Status.ERROR, mark.elapsedNow(), message)
            else -> TestResult(testNr, TestResult.Status.ERROR, mark.elapsedNow(), "COULDN'T FIND STATUS")
        }
    } finally {
        Runtime.getRuntime().removeShutdownHook(hook)
    }
}

data class TestResult(
    val testNumber: Int,
    val status: Status,
    val duration: Duration,
    val msg: String,
) {
    enum class Status {
        SUCCESS,
        DEADLOCK,
        TIMEOUT,
        ERROR,
    }
}


// AI generated slop here 
private fun findRunCheckerClasses(): Map<Int, KClass<out RunCheckerBase>> {
    val result = mutableMapOf<Int, KClass<out RunCheckerBase>>()

    val classLoader = Thread.currentThread().contextClassLoader
    val packageName = "org.example.altered"

    // Find all classes in the package and its subpackages
    val packagePath = packageName.replace('.', '/')
    val resources = classLoader.getResources(packagePath)

    while (resources.hasMoreElements()) {
        val resource = resources.nextElement()
        val directory = File(resource.file)

        if (directory.exists()) {
            findClassesInDirectory(directory, packageName, result)
        }
    }

    return result
}

private fun findClassesInDirectory(directory: File, packageName: String, result: MutableMap<Int, KClass<out RunCheckerBase>>) {
    directory.listFiles()?.forEach { file ->
        if (file.isDirectory) {
            findClassesInDirectory(file, "$packageName.${file.name}", result)
        } else if (file.name.endsWith(".class")) {
            val className = file.name.substring(0, file.name.length - 6)
            try {
                val fullClassName = "$packageName.$className"
                val kClass = Class.forName(fullClassName).kotlin

                if (kClass.isSubclassOf(RunCheckerBase::class) && className.startsWith("RunChecker")) {
                    val testNumber = className.removePrefix("RunChecker").toIntOrNull()
                    if (testNumber != null) {
                        @Suppress("UNCHECKED_CAST")
                        result[testNumber] = kClass as KClass<out RunCheckerBase>
                    }
                }
            } catch (e: ClassNotFoundException) {
                // Ignore
            }
        }
    }
}
