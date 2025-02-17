/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":3,"nChannels":6,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 6 different channels
- 3 different coroutines
- 0 different classes

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
package org.example.altered.test450
import org.example.altered.test450.RunChecker450.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

fun function1(chan1: Channel<Int>, chan2: Channel<Int>) {
    runBlocking(pool) {
        launch(pool) {
            val data = chan1.receive()
            chan2.send(data)
        }
    }
}

fun function2(chan3: Channel<Int>, chan4: Channel<Int>) {
    runBlocking(pool) {
        launch(pool) {
            val data = chan3.receive()
            chan4.send(data)
        }
    }
}

fun function3(chan5: Channel<Int>, chan6: Channel<Int>) {
    runBlocking(pool) {
        launch(pool) {
            val data = chan5.receive()
            chan6.send(data)
        }
    }
}

fun main(): Unit{
    val chan1 = Channel<Int>()
    val chan2 = Channel<Int>()
    val chan3 = Channel<Int>()
    val chan4 = Channel<Int>()
    val chan5 = Channel<Int>()
    val chan6 = Channel<Int>()

    runBlocking(pool) {
        // Coroutine 1
        launch(pool) {
            chan1.send(42)
            function1(chan1, chan2)
        }

        // Coroutine 2
        launch(pool) {
            chan3.send(43)
            function2(chan3, chan4)
        }

        // Coroutine 3
        launch(pool) {
            chan5.send(44)
            function3(chan5, chan6)
        }

        // Deadlock creation
        launch(pool) {
            val result1 = chan2.receive()
            chan4.send(result1)

            val result2 = chan4.receive()
            chan6.send(result2)

            val result3 = chan6.receive()
            chan2.send(result3) // This send will cause a deadlock
        }
    }
}

class RunChecker450: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}