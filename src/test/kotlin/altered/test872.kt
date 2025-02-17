/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":1,"nChannels":2,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 2 different channels
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
package org.example.altered.test872
import org.example.altered.test872.RunChecker872.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Processor(val inputChannel: Channel<Int>, val outputChannel: Channel<Int>) {
    suspend fun process() {
        val data = inputChannel.receive()
        outputChannel.send(data * 2)
    }
}

fun producer(channel: Channel<Int>) {
    runBlocking(pool) {
        channel.send(10)
    }
}

fun consumer(channel: Channel<Int>) {
    runBlocking(pool) {
        val result = channel.receive()
        println("Result: $result")
    }
}

fun main(): Unit= runBlocking(pool) {
    val inputChannel = Channel<Int>()
    val outputChannel = Channel<Int>()

    val processor = Processor(inputChannel, outputChannel)

    launch(pool) {
        producer(inputChannel)
    }

    launch(pool) {
        processor.process()
    }

    launch(pool) {
        consumer(outputChannel)
    }
}

class RunChecker872: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}