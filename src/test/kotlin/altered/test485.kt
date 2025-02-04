/* 
{"deadlock":false,"nFunctions":8,"nCoroutines":6,"nChannels":3,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
- 3 different channels
- 6 different coroutines
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
package org.example.altered.test485
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(private val channel: Channel<Int>) {
    suspend fun produceData1() {
        for (i in 1..5) {
            channel.send(i)
        }
    }

    suspend fun produceData2() {
        for (i in 6..10) {
            channel.send(i)
        }
    }
}

class Consumer(private val channel: Channel<Int>) {
    suspend fun consumeData1() {
        repeat(5) {
            println("Consumed: ${channel.receive()}")
        }
    }

    suspend fun consumeData2() {
        repeat(5) {
            println("Consumed: ${channel.receive()}")
        }
    }
}

class Aggregator(private val channel1: Channel<Int>, private val channel2: Channel<Int>) {
    suspend fun aggregateData() {
        repeat(5) {
            val data1 = channel1.receive()
            val data2 = channel2.receive()
            println("Aggregated: ${data1 + data2}")
        }
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()

    val producer1 = Producer(channel1)
    val producer2 = Producer(channel2)

    val consumer1 = Consumer(channel1)
    val consumer2 = Consumer(channel2)

    val aggregator = Aggregator(channel1, channel3)

    launch { producer1.produceData1() }
    launch { producer2.produceData2() }
    launch { consumer1.consumeData1() }
    launch { consumer2.consumeData2() }
    launch { aggregator.aggregateData() }
    launch {
        repeat(5) {
            channel3.send(it) // Additional producer for channel3
        }
    }
}

class RunChecker485: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}