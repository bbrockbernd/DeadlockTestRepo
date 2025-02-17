/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":2,"nChannels":1,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 1 different channels
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
package org.example.altered.test149
import org.example.altered.test149.RunChecker149.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Processor(val channel: Channel<Int>) {
    suspend fun processData() {
        for (i in 1..5) {
            channel.send(i)
        }
    }
}

fun startProcessing(channel: Channel<Int>) = runBlocking(pool) {
    launch(pool) {
        val processor = Processor(channel)
        processor.processData()
    }
}

fun receiveData(channel: Channel<Int>) = runBlocking(pool) {
    launch(pool) {
        while (true) {
            val data = channel.receive()
            println("Received: $data")
        }
    }
}

fun main(): Unit{
    val channel = Channel<Int>()
    startProcessing(channel)
    receiveData(channel)
}

class RunChecker149: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}