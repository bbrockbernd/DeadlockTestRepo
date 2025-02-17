/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":2,"nChannels":2,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
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
package org.example.altered.test853
import org.example.altered.test853.RunChecker853.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.*

class Worker {
    suspend fun work(channel1: Channel<Int>, channel2: Channel<Int>) {
        val data = channel1.receive()
        channel2.send(data * 2)
    }
}

fun functionOne(channel1: Channel<Int>, channel2: Channel<Int>) = runBlocking(pool) {
    coroutineScope {
        launch(pool) {
            val worker = Worker()
            worker.work(channel1, channel2)
        }
    }
}

fun functionTwo(channel: Channel<Int>) = runBlocking(pool) {
    channel.send(42)
}

fun functionThree(channel: Channel<Int>) = runBlocking(pool) {
    val result = channel.receive()
    println("Result: $result")
}

fun main(): Unit= runBlocking(pool) {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()

    launch(pool) {
        functionOne(channel1, channel2)
    }

    launch(pool) {
        functionTwo(channel1)
    }

    functionThree(channel2)
}

class RunChecker853: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}