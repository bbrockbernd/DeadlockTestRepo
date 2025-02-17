/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":4,"nChannels":2,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 2 different channels
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
package org.example.altered.test661
import org.example.altered.test661.RunChecker661.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class TestClass {
    private val channel1 = Channel<Int>()
    private val channel2 = Channel<Int>()

    fun runTest() = runBlocking(pool) {
        launch(pool) { coroutine1() }
        launch(pool) { coroutine2() }
        launch(pool) { coroutine3() }
        launch(pool) { coroutine4() }
    }

    private suspend fun coroutine1() {
        channel1.send(1)
        println("coroutine1 sent to channel1")
        channel2.send(channel1.receive())
        println("coroutine1 sent to channel2")
    }

    private suspend fun coroutine2() {
        println("coroutine2 waiting to receive from channel2")
        val received = channel2.receive()
        println("coroutine2 received: $received")
    }

    private suspend fun coroutine3() {
        coroutineScope {
            println("coroutine3 waiting to receive from channel1")
            val received = channel1.receive()
            println("coroutine3 received: $received")
        }
    }

    private suspend fun coroutine4() {
        println("coroutine4 waiting to send to channel2")
        channel2.send(2)
        println("coroutine4 sent to channel2")
    }
}

fun main(): Unit{
    TestClass().runTest()
}

class RunChecker661: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}