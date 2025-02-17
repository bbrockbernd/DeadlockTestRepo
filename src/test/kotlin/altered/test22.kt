/* 
{"deadlock":true,"nFunctions":8,"nCoroutines":5,"nChannels":6,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
- 6 different channels
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
package org.example.altered.test22
import org.example.altered.test22.RunChecker22.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class DeadlockExample {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()
    val channel6 = Channel<Int>()

    fun function1() = runBlocking(pool) {
        coroutineScope {
            launch(pool) { function2() }
            launch(pool) { function3() }
            launch(pool) { function4() }
            launch(pool) { function5() }
            launch(pool) { function6() }
        }
    }

    suspend fun function2() {
        val result = channel2.receive()
        channel1.send(result)
        function7()
    }

    suspend fun function3() {
        channel2.send(10)
        val result = channel3.receive()
        channel1.send(result)
    }

    suspend fun function4() {
        val result = channel4.receive()
        channel3.send(result)
        function8()
    }

    suspend fun function5() {
        channel4.send(20)
        val result = channel5.receive()
        channel3.send(result)
    }

    suspend fun function6() {
        val result = channel6.receive()
        channel5.send(result)
    }

    suspend fun function7() {
        val result = channel1.receive()
        channel6.send(result)
    }

    suspend fun function8() {
        val result = channel1.receive()
        channel6.send(result)
    }
}

fun main(): Unit{
    val deadlockExample = DeadlockExample()
    deadlockExample.function1()
}

class RunChecker22: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}