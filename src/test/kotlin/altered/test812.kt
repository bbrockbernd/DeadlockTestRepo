/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":2,"nChannels":2,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 2 different channels
- 2 different coroutines
- 3 different classes

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
package org.example.altered.test812
import org.example.altered.test812.RunChecker812.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class Producer {
    suspend fun produce(channel: Channel<Int>) {
        for (i in 1..5) {
            channel.send(i)
        }
        channel.close()
    }
}

class Processor {
    suspend fun process(channelIn: Channel<Int>, channelOut: Channel<Int>) {
        for (value in channelIn) {
            channelOut.send(value * 2)
        }
        channelOut.close()
    }
}

class Consumer {
    suspend fun consume(channel: Channel<Int>): List<Int> {
        val results = mutableListOf<Int>()
        for (value in channel) {
            results.add(value)
        }
        return results
    }
}

fun main(): Unit= runBlocking(pool) {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val producer = Producer()
    val processor = Processor()
    val consumer = Consumer()

    launch(pool) {
        producer.produce(channel1)
    }

    launch(pool) {
        processor.process(channel1, channel2)
    }

    val results = consumer.consume(channel2)
    println(results)
}

class RunChecker812: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}