/* 
{"deadlock":true,"nFunctions":8,"nCoroutines":3,"nChannels":4,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
- 4 different channels
- 3 different coroutines
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
package org.example.altered.test316
import org.example.altered.test316.RunChecker316.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class DeadlockTest {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()

    fun function1() = runBlocking(pool) {
        launch(pool) { sendToChannel1() }
        launch(pool) { receiveFromChannel2() }
    }

    fun function2() = runBlocking(pool) {
        launch(pool) { sendToChannel2() }
        launch(pool) { receiveFromChannel3() }
    }

    fun function3() = runBlocking(pool) {
        launch(pool) { sendToChannel3() }
        launch(pool) { receiveFromChannel4() }
    }

    fun function4() = runBlocking(pool) {
        launch(pool) { sendToChannel4() }
        launch(pool) { receiveFromChannel1() }
    }

    suspend fun sendToChannel1() {
        channel1.send(1)
    }

    suspend fun sendToChannel2() {
        channel2.send(2)
    }

    suspend fun sendToChannel3() {
        channel3.send(3)
    }

    suspend fun sendToChannel4() {
        channel4.send(4)
    }

    suspend fun receiveFromChannel1() {
        channel1.receive()
    }

    suspend fun receiveFromChannel2() {
        channel2.receive()
    }

    suspend fun receiveFromChannel3() {
        channel3.receive()
    }

    suspend fun receiveFromChannel4() {
        channel4.receive()
    }
}

fun main(): Unit {
    val deadlockTest = DeadlockTest()
    deadlockTest.function1()
    deadlockTest.function2()
    deadlockTest.function3()
    deadlockTest.function4()
}

class RunChecker316: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}