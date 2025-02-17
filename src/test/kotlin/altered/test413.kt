/* 
{"deadlock":true,"nFunctions":8,"nCoroutines":4,"nChannels":1,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
- 1 different channels
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
package org.example.altered.test413
import org.example.altered.test413.RunChecker413.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class DeadlockExample(private val channel: Channel<Int>) {

    fun initiateCoroutines() {
        runBlocking(pool) {
            launch(pool) { coroutine1() }
            launch(pool) { coroutine2() }
            launch(pool) { coroutine3() }
            launch(pool) { coroutine4() }
        }
    }

    suspend fun coroutine1() {
        function1()
        function2()
    }

    suspend fun coroutine2() {
        function3()
        function4()
    }

    suspend fun coroutine3() {
        function5()
        function6()
    }

    suspend fun coroutine4() {
        function7()
        function8()
    }

    suspend fun function1() = coroutineScope {
        val data = 1
        channel.send(data)
    }

    suspend fun function2() = coroutineScope {
        val received = channel.receive()
        if (received == 2) {
            channel.send(3)
        }
    }

    suspend fun function3() = coroutineScope {
        channel.send(2)
    }

    suspend fun function4() = coroutineScope {
        val received = channel.receive()
        if (received == 3) {
            channel.send(4)
        }
    }

    suspend fun function5() = coroutineScope {
        val data = channel.receive()
        if (data == 1) {
            channel.send(2)
        }
    }

    suspend fun function6() = coroutineScope {
        channel.send(3)
    }

    suspend fun function7() = coroutineScope {
        val received = channel.receive()
        if (received == 4) {
            channel.send(5)
        }
    }

    suspend fun function8() = coroutineScope {
        channel.send(1)
    }
}

fun main(): Unit{
    val channel = Channel<Int>()
    val example = DeadlockExample(channel)
    example.initiateCoroutines()
}

class RunChecker413: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}