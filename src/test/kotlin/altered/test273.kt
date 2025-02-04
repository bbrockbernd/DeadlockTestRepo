/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":6,"nChannels":1,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 1 different channels
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
package org.example.altered.test273
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(private val channel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            println("Produced: $i")
            channel.send(i)
        }
    }
}

class Consumer(private val channel: Channel<Int>) {
    suspend fun consume() {
        for (i in 1..5) {
            val value = channel.receive()
            println("Consumed: $value")
        }
    }
}

class Processor(private val channel: Channel<Int>) {
    suspend fun process() {
        for (i in 1..5) {
            val value = channel.receive()
            println("Processed: $value")
            channel.send(value * 2)
        }
    }
}

fun initProducer(channel: Channel<Int>): Producer {
    return Producer(channel)
}

fun initConsumer(channel: Channel<Int>): Consumer {
    return Consumer(channel)
}

fun initProcessor(channel: Channel<Int>): Processor {
    return Processor(channel)
}

fun main(): Unit= runBlocking {
    val channel = Channel<Int>()  // Unbuffered channel

    val producer = initProducer(channel)
    val consumer = initConsumer(channel)
    val processor = initProcessor(channel)

    launch { producer.produce() }
    launch { processor.process() }
    launch { consumer.consume() }
    launch { processor.process() } // Duplicate process creation to cause deadlock
    launch { consumer.consume() } // Duplicate consumer creation to cause deadlock
    launch { producer.produce() } // Duplicate producer creation to cause deadlock
}

class RunChecker273: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}