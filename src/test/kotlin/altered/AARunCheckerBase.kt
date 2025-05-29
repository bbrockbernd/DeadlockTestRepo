package org.example.altered

import org.jetbrains.kotlinx.lincheck.Lincheck.runConcurrentTest
import kotlin.test.Test


abstract class RunCheckerBase {
    abstract fun block()
    @Test
    fun GPMChecker() {
//        val mark = TimeSource.Monotonic.markNow()
        // run test and catch any error or assertion
//        val result = runCatching {
            runConcurrentTest {
//                if (mark.elapsedNow() > 30.seconds) throw TimeExceededException()
                block()
                // inject timout by throwing exception 
            }
//        }

//        // If test passed (without timeout) return
//        if (!result.isFailure) return
//        val exception = result.exceptionOrNull()
//
//        if (exception is LincheckAssertionError) {
//            val actualResult = exception.failure.results.threadsResults[0][0]
//            if (actualResult is ExceptionResult && actualResult.throwable is TimeExceededException) return
//        }
//        
//        // If deadlock fail test
//        if (exception?.message?.contains("Concurrent test has hung") == true) {
//            throw AssertionError(exception.message)
//        }
//
//        // else crash test
//        throw IllegalStateException("Unexpected test result: ${exception?.message ?: "No message"}")
    }
}

//class TimeExceededException : IllegalStateException()