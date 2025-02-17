/* 
{"deadlock":true,"nFunctions":2,"nCoroutines":4,"nChannels":2,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
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
package org.example.altered.test866
import org.example.altered.test866.RunChecker866.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Processor(val channel1: Channel<Int>, val channel2: Channel<Int>) {
    
    suspend fun processChannel1() {
        val value = channel1.receive() 
        channel2.send(value)
    }

    suspend fun processChannel2() {
        val value = channel2.receive()
        channel1.send(value)
    }
}

fun main(): Unit= runBlocking(pool) {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val processor = Processor(channel1, channel2)

    // Coroutine 1: Processes from channel1 to channel2
    launch(pool) {
        processor.processChannel1()
    }

    // Coroutine 2: Processes from channel2 to channel1
    launch(pool) {
        processor.processChannel2()
    }

    // Coroutine 3: Sends initial value to channel1
    launch(pool) {
        channel1.send(1)
    }

    // Coroutine 4: Intends to send initial value to channel2
    launch(pool) {
        channel2.send(2)
    }
}

class RunChecker866: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}