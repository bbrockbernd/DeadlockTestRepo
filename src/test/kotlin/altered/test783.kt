/* 
{"deadlock":false,"nFunctions":1,"nCoroutines":5,"nChannels":2,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
- 2 different channels
- 5 different coroutines
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
package org.example.altered.test783
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(private val channel1: Channel<String>) {
    suspend fun produceMessage(message: String) {
        channel1.send(message)
    }
}

class Consumer(private val channel1: Channel<String>, private val channel2: Channel<String>) {
    suspend fun consumeMessage() {
        val message = channel1.receive()
        channel2.send("Processed: $message")
    }
}

fun producerJob(channel1: Channel<String>) = CoroutineScope(Dispatchers.Default).launch {
    val producer = Producer(channel1)
    producer.produceMessage("Hello from producer")
}

fun consumerJob(channel1: Channel<String>, channel2: Channel<String>) = CoroutineScope(Dispatchers.Default).launch {
    val consumer = Consumer(channel1, channel2)
    consumer.consumeMessage()
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<String>()
    val channel2 = Channel<String>()

    launch {
        producerJob(channel1)
    }

    launch {
        consumerJob(channel1, channel2)
    }

    launch {
        val consumer = Consumer(channel1, channel2)
        consumer.consumeMessage()
    }

    launch {
        println("Final message: ${channel2.receive()}")
    }
}

class RunChecker783: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}