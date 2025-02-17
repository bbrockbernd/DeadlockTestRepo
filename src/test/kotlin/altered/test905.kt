/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":3,"nChannels":5,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 5 different channels
- 3 different coroutines
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
package org.example.altered.test905
import org.example.altered.test905.RunChecker905.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class Processor {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()

    suspend fun produceNumbers() {
        for (i in 1..5) {
            channel1.send(i)
            delay(10)
        }
        channel1.close()
    }
}

class Aggregator {
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>(2)

    suspend fun aggregateNumbers(processor: Processor) {
        for (x in processor.channel1) {
            val result = x * 2
            channel3.send(result)
        }
        channel3.close()
    }

    suspend fun sendToChannels() {
        for (x in channel3) {
            channel4.send(x)
            channel5.send(x)
        }
        channel4.close()
        channel5.close()
    }
}

suspend fun receiveAndPrint(channel: Channel<Int>) {
    for (y in channel) {
        println("Received: $y")
    }
}

fun main(): Unit= runBlocking(pool) {
    val processor = Processor()
    val aggregator = Aggregator()

    launch(pool) { processor.produceNumbers() }
    launch(pool) { aggregator.aggregateNumbers(processor) }
    launch(pool) { aggregator.sendToChannels() }

    launch(pool) { receiveAndPrint(aggregator.channel4) }
    launch(pool) { receiveAndPrint(aggregator.channel5) }
}

class RunChecker905: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}