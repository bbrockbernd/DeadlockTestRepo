/* 
{"deadlock":true,"nFunctions":2,"nCoroutines":1,"nChannels":4,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 4 different channels
- 1 different coroutines
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
package org.example.altered.test312
import org.example.altered.test312.RunChecker312.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class DeadlockExample {
    private val channel1 = Channel<Int>()
    private val channel2 = Channel<Int>()
    private val channel3 = Channel<Int>()
    private val channel4 = Channel<Int>()

    suspend fun functionOne() {
        coroutineScope {
            launch(pool) {
                val result1 = channel1.receive()
                channel2.send(result1)

                val result3 = channel3.receive()
                channel4.send(result3)
            }
        }
    }

    suspend fun functionTwo() {
        coroutineScope {
            launch(pool) {
                val result4 = channel4.receive()
                channel3.send(result4)

                val result2 = channel2.receive()
                channel1.send(result2)
            }
        }
    }

    fun start() = runBlocking(pool) {
        launch(pool) {
            functionOne()
        }
        launch(pool) {
            functionTwo()
        }

        channel1.send(42)
    }
}

fun main(): Unit{
    DeadlockExample().start()
}

class RunChecker312: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}