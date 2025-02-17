/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":4,"nChannels":2,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
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
package org.example.altered.test898
import org.example.altered.test898.RunChecker898.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ChannelTester(val channel1: Channel<Int>, val channel2: Channel<Int>) {
    suspend fun functionA() {
        val value = channel1.receive()
        channel2.send(value)
    }

    suspend fun functionB() {
        val value = channel2.receive()
        channel1.send(value)
    }

    suspend fun functionC() {
        val value = channel1.receive()
        channel1.send(value)
    }

    suspend fun functionD() {
        val value = channel2.receive()
        channel2.send(value)
    }
}

fun main(): Unit= runBlocking(pool) {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val tester = ChannelTester(channel1, channel2)

    launch(pool) { tester.functionA() }
    launch(pool) { tester.functionB() }
    launch(pool) { tester.functionC() }
    launch(pool) { tester.functionD() }

    channel1.send(1)
}

class RunChecker898: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}