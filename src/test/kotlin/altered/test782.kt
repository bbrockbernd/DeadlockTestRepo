/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":2,"nChannels":2,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 2 different channels
- 2 different coroutines
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
package org.example.altered.test782
import org.example.altered.test782.RunChecker782.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Processor {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()

    suspend fun sendToChannel1() {
        channel1.send(1)
        channel2.receive()
    }

    suspend fun sendToChannel2() {
        channel2.send(2)
        channel1.receive()
    }

    suspend fun processChannel1() {
        channel1.receive()
        sendToChannel2()
    }

    suspend fun processChannel2() {
        channel2.receive()
        sendToChannel1()
    }
}

fun main(): Unit= runBlocking(pool) {
    val processor = Processor()

    launch(pool) {
        processor.processChannel1()
    }

    launch(pool) {
        processor.processChannel2()
    }
}

class RunChecker782: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}