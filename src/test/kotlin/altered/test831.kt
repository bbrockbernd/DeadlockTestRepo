/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":1,"nChannels":3,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 3 different channels
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
package org.example.altered.test831
import org.example.altered.test831.RunChecker831.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class DeadlockDetector {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()
    val channelC = Channel<Int>()

    fun start() = runBlocking(pool) {
        launch(pool) { function1() }
        function2()
    }

    suspend fun function1() {
        channelA.send(1)
        delay(100)
        val received = channelB.receive()
        println("Function 1 received: $received")
    }

    suspend fun function2() {
        val received = channelA.receive()
        println("Function 2 received: $received")
        channelB.send(2)
        function3()
    }

    suspend fun function3() {
        val received = channelB.receive()
        println("Function 3 received: $received")
        channelC.send(3)
        function4()
    }

    suspend fun function4() {
        val received = channelC.receive()
        println("Function 4 received: $received")
    }
}

fun main(): Unit{
    val detector = DeadlockDetector()
    detector.start()
}

class RunChecker831: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}