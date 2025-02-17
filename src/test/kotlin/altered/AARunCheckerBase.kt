package org.example.altered

import org.jetbrains.kotlinx.lincheck.ExperimentalModelCheckingAPI
import kotlin.test.Test
import org.jetbrains.kotlinx.lincheck.runConcurrentTest


abstract class RunCheckerBase {
    abstract fun block()
    @OptIn(ExperimentalModelCheckingAPI::class)
    @Test
    fun GPMChecker() {
        runConcurrentTest { block() }
    }
}