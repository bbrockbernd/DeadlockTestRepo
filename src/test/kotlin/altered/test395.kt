/* 
{"deadlock":false,"nFunctions":6,"nCoroutines":5,"nChannels":6,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 6 different channels
- 5 different coroutines
- 4 different classes

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
package org.example.altered.test395
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer1(private val channel: Channel<Int>) {
    suspend fun produce() {
        repeat(5) {
            channel.send(it)
        }
    }
}

class Producer2(private val channel: Channel<Int>) {
    suspend fun produce() {
        repeat(5) {
            channel.send(it + 10)
        }
    }
}

class Consumer1(private val channel: Channel<Int>) {
    suspend fun consume() {
        repeat(5) {
            println("Consumer1 received: ${channel.receive()}")
        }
    }
}

class Consumer2(private val channel: Channel<Int>) {
    suspend fun consume() {
        repeat(5) {
            println("Consumer2 received: ${channel.receive()}")
        }
    }
}

val channel1 = Channel<Int>()
val channel2 = Channel<Int>()
val channel3 = Channel<Int>(5)
val channel4 = Channel<Int>(5)
val channel5 = Channel<Int>()
val channel6 = Channel<Int>()

suspend fun runProducer1() {
    val producer1 = Producer1(channel1)
    producer1.produce()
}

suspend fun runProducer2() {
    val producer2 = Producer2(channel2)
    producer2.produce()
}

suspend fun runConsumer1() {
    val consumer1 = Consumer1(channel1)
    consumer1.consume()
}

suspend fun runConsumer2() {
    val consumer2 = Consumer2(channel2)
    consumer2.consume()
}

suspend fun channelCopy(source: Channel<Int>, destination: Channel<Int>) {
    repeat(5) {
        destination.send(source.receive())
    }
}

fun main(): Unit= runBlocking {
    launch {
        runProducer1()
    }
    launch {
        runConsumer1()
    }
    launch {
        runProducer2()
    }
    launch {
        runConsumer2()
    }
    launch {
        channelCopy(channel1, channel3)
    }
}

class RunChecker395: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}