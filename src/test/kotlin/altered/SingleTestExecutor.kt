package altered

import org.example.altered.RunCheckerBase
import org.jetbrains.kotlinx.lincheck.ExperimentalModelCheckingAPI
import org.jetbrains.kotlinx.lincheck.runConcurrentTest
import kotlin.reflect.full.createInstance

@OptIn(ExperimentalModelCheckingAPI::class)
class SingleTestExecutor {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            if (args.size != 1) {
                println("Usage: SingleTestRunner <testNumber>")
                return
            }

            val nr = args[0].toInt()
            val kClass = Class.forName("org.example.altered.test$nr.RunChecker$nr").kotlin
            val instance = kClass.createInstance() as RunCheckerBase

            val result = runCatching {
                runConcurrentTest { instance.block() }
            }

            println("DONE")
            
            if (!result.isFailure) {
                println(TestResult.Status.SUCCESS.name)
                return
            }

            val exception = result.exceptionOrNull()

            if (exception?.message?.contains("Concurrent test has hung") == true) {
                println(TestResult.Status.DEADLOCK.name)
            } else println(TestResult.Status.ERROR.name)

            println(exception?.message)
        }
    }
}