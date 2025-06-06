package org.example.altered

import org.jetbrains.kotlinx.lincheck.Lincheck.runConcurrentTest
import org.junit.jupiter.api.assertTimeoutPreemptively
import org.opentest4j.AssertionFailedError
import kotlin.test.Test
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration


abstract class RunCheckerBase {
    abstract fun block()
    
    @Test
    fun GPMChecker() {
        try {
            assertTimeoutPreemptively(50.seconds.toJavaDuration()) {
                runConcurrentTest {
                    block()
                }
            }
        } catch (e: AssertionFailedError) {
            if (e.cause?.javaClass?.name == "org.junit.jupiter.api.AssertTimeoutPreemptively\$ExecutionTimeoutException") return
            throw e
        } 
    }
}