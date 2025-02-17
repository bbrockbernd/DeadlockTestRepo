/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":4,"nChannels":4,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 4 different channels
- 4 different coroutines
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
package org.example.altered.test596
import org.example.altered.test596.RunChecker596.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class DeadlockTest {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()

    fun start() = runBlocking(pool) {
        launch(pool) { function1() }
        launch(pool) { function2() }
        launch(pool) { function3() }
        launch(pool) { function4() }
    }

    suspend fun function1() {
        val x = channel1.receive()
        channel2.send(x)
    }

    suspend fun function2() {
        val x = channel2.receive()
        channel3.send(x)
    }

    suspend fun function3() {
        val x = channel3.receive()
        channel4.send(x)
    }

    suspend fun function4() {
        val x = channel4.receive()
        function5(x)
    }

    suspend fun function5(x: Int) {
        channel1.send(x)
    }
}

fun main(): Unit{
    val test = DeadlockTest()
    test.start()
}

class RunChecker596: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}