/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":3,"nChannels":3,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 3 different channels
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
package org.example.altered.test612
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class Producer(val channel: Channel<Int>) {
    suspend fun produce(value: Int) {
        channel.send(value)
    }
}

class Consumer(val channel: Channel<Int>) {
    suspend fun consume(): Int {
        return channel.receive()
    }
}

suspend fun produceValues(producer: Producer) {
    producer.produce(1)
    producer.produce(2)
    producer.produce(3)
}

suspend fun consumeValues(consumer: Consumer) {
    println(consumer.consume())
    println(consumer.consume())
    println(consumer.consume())
}

suspend fun transferValues(channel1: Channel<Int>, channel2: Channel<Int>) {
    val value = channel1.receive()
    channel2.send(value)
}

fun main(): Unit= runBlocking {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()
    val channelC = Channel<Int>()

    val producer = Producer(channelA)
    val consumer = Consumer(channelC)

    launch {
        produceValues(producer)
    }

    launch {
        transferValues(channelA, channelB)
    }

    launch {
        transferValues(channelB, channelC)
        // Intentional deadlock: This coroutine will wait indefinitely as transferValues in another coroutine won't be able to send value to this channel due to deadlock
    }

    consumeValues(consumer)
}

class RunChecker612: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}