/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":5,"nChannels":5,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 5 different channels
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
package org.example.altered.test886
import org.example.altered.test886.RunChecker886.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class DeadlockExample {
    private val channel1 = Channel<Int>()
    private val channel2 = Channel<Int>()
    private val channel3 = Channel<Int>()
    private val channel4 = Channel<Int>()
    private val channel5 = Channel<Int>()

    fun function1() {
        runBlocking(pool) {
            launch(pool) {
                channel1.send(1)
                channel2.receive()
            }
        }
    }

    fun function2() {
        runBlocking(pool) {
            launch(pool) {
                channel2.send(2)
                channel3.receive()
            }
        }
    }

    fun function3() {
        runBlocking(pool) {
            launch(pool) {
                channel3.send(3)
                channel4.receive()
            }
        }
    }

    fun function4() {
        runBlocking(pool) {
            launch(pool) {
                channel4.send(4)
                channel5.receive()
            }
        }
    }

    fun function5() {
        runBlocking(pool) {
            launch(pool) {
                channel5.send(5)
                channel1.receive()
            }
        }
    }
}

fun main(): Unit{
    val deadlockExample = DeadlockExample()
    runBlocking(pool) {
        launch(pool) { deadlockExample.function1() }
        launch(pool) { deadlockExample.function2() }
        launch(pool) { deadlockExample.function3() }
        launch(pool) { deadlockExample.function4() }
        launch(pool) { deadlockExample.function5() }
    }
}

class RunChecker886: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}