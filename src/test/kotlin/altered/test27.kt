/* 
{"deadlock":true,"nFunctions":6,"nCoroutines":6,"nChannels":3,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 3 different channels
- 6 different coroutines
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
package org.example.altered.test27
import org.example.altered.test27.RunChecker27.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class CoroutineExample {

    private val channel1 = Channel<Int>()
    private val channel2 = Channel<Int>()
    private val channel3 = Channel<Int>()

    fun start() = runBlocking(pool) {
        launch(pool) { function1() }
        launch(pool) { function2() }
        launch(pool) { function3() }
        launch(pool) { function4() }
        launch(pool) { function5() }
        launch(pool) { function6() }
    }

    suspend fun function1() {
        coroutineScope {
            val value = channel1.receive()
            channel2.send(value)
        }
    }

    suspend fun function2() {
        coroutineScope {
            val value = channel2.receive()
            channel3.send(value)
        }
    }

    suspend fun function3() {
        coroutineScope {
            val value = channel3.receive()
            channel1.send(value)
        }
    }

    suspend fun function4() {
        coroutineScope {
            channel1.send(1)
            val value = channel3.receive()
        }
    }

    suspend fun function5() {
        coroutineScope {
            channel2.send(2)
            val value = channel1.receive()
        }
    }

    suspend fun function6() {
        coroutineScope {
            channel3.send(3)
            val value = channel2.receive()
        }
    }
}

fun main(): Unit{
    CoroutineExample().start()
}

class RunChecker27: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}