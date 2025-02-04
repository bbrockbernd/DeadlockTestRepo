/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":7,"nChannels":6,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 6 different channels
- 7 different coroutines
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
package org.example.altered.test484
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer1(private val channel1: Channel<Int>, private val channel2: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            channel1.send(i)
            channel2.send(i * 2)
        }
    }
}

class Producer2(private val channel3: Channel<Int>, private val channel4: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            channel3.send(i * 3)
            channel4.send(i * 4)
        }
    }
}

class Consumer(private val channel: Channel<Int>, private val channelCollector: Channel<Int>) {
    suspend fun consume() {
        for (i in 1..5) {
            val value = channel.receive()
            channelCollector.send(value)
        }
    }
}

suspend fun collector(channelCollector1: Channel<Int>, channelCollector2: Channel<Int>): Int {
    var sum = 0
    for (i in 1..5) {
        sum += channelCollector1.receive()
        sum += channelCollector2.receive()
    }
    return sum
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channelCollector1 = Channel<Int>()
    val channelCollector2 = Channel<Int>()

    val producer1 = Producer1(channel1, channel2)
    val producer2 = Producer2(channel3, channel4)
    val consumer1 = Consumer(channel1, channelCollector1)
    val consumer2 = Consumer(channel2, channelCollector1)
    val consumer3 = Consumer(channel3, channelCollector2)
    val consumer4 = Consumer(channel4, channelCollector2)

    launch { producer1.produce() }
    launch { producer2.produce() }
    launch { consumer1.consume() }
    launch { consumer2.consume() }
    launch { consumer3.consume() }
    launch { consumer4.consume() }

    val result = collector(channelCollector1, channelCollector2)
    println("Collected sum: $result")
}

class RunChecker484: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}