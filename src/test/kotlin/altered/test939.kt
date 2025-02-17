/* 
{"deadlock":false,"nFunctions":1,"nCoroutines":4,"nChannels":3,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
- 3 different channels
- 4 different coroutines
- 2 different classes

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
package org.example.altered.test939
import org.example.altered.test939.RunChecker939.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(private val channel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            channel.send(i)
        }
    }
}

class Consumer(private val channel: Channel<Int>, private val outputChannel: Channel<Int>) {
    suspend fun consume() {
        for (i in 1..5) {
            val item = channel.receive()
            outputChannel.send(item * 2)
        }
    }
}

suspend fun process(outputChannel: Channel<Int>) {
    for (i in 1..5) {
        val processedItem = outputChannel.receive()
        println("Processed Item: $processedItem")
    }
}

fun main(): Unit= runBlocking(pool) {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()

    val producer = Producer(channel1)
    val consumer1 = Consumer(channel1, channel2)
    val consumer2 = Consumer(channel2, channel3)

    launch(pool) { producer.produce() }
    launch(pool) { consumer1.consume() }
    launch(pool) { consumer2.consume() }
    launch(pool) { process(channel3) }
}

class RunChecker939: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}