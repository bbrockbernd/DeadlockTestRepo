/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":5,"nChannels":3,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 3 different channels
- 5 different coroutines
- 1 different classes

You ARE ALLOWED to use basic Kotlin constructs and coroutine primitives. A few examples are:
- functions and suspend functions
- dot qualified expressions
- class properties
- local variables
- unbuffered and buffered channels
- channel init, send and receive
- return values
- function composition (nested calls)
- runBlocking and launch builder
- coroutineScope

You ARE NOT ALLOWED to use more complex features like:
- joins
- async builder
- lists, arrays or other datastructures
- mutability
- nullability
- for (i in channel)
- flow
- lateinit
- lazyval
- inheritance
- lambdas with arguments
- mutexes 
*/
package org.example.altered.test914
import org.example.altered.test914.RunChecker914.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class FooBar {
    private val channel1 = Channel<Int>()
    private val channel2 = Channel<Int>()
    private val channel3 = Channel<Int>()

    suspend fun foo() {
        val x = channel2.receive()
        channel1.send(x)
    }

    suspend fun bar() {
        val y = channel1.receive()
        channel3.send(y)
    }

    suspend fun baz() {
        val z = channel3.receive()
        channel2.send(z)
    }

    fun launchCoroutines() {
        runBlocking(pool) {
            launch(pool) { alpha() }
            launch(pool) { beta() }
            launch(pool) { gamma() }
            launch(pool) { delta() }
            launch(pool) { epsilon() }
        }
    }

    private suspend fun alpha() {
        channel1.send(1)
    }

    private suspend fun beta() {
        foo()
    }

    private suspend fun gamma() {
        bar()
    }

    private suspend fun delta() {
        baz()
    }

    private suspend fun epsilon() {
        channel3.send(2)
    }
}

fun main(): Unit{
    FooBar().launchCoroutines()
}

class RunChecker914: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}