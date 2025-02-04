/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":4,"nChannels":5,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 5 different channels
- 4 different coroutines
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
package org.example.altered.test429
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class ProducerA(private val channel: Channel<String>) {
    suspend fun produce() {
        channel.send("From ProducerA")
    }
}

class ProducerB(private val channel: Channel<String>) {
    suspend fun produce() {
        channel.send("From ProducerB")
    }
}

class ConsumerA(private val channel: Channel<String>) {
    suspend fun consume() {
        val message = channel.receive()
        println(message)
    }
}

class ConsumerB(private val channel: Channel<String>) {
    suspend fun consume() {
        val message = channel.receive()
        println(message)
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<String>()
    val channel2 = Channel<String>()
    val channel3 = Channel<String>()
    val channel4 = Channel<String>()
    val channel5 = Channel<String>()

    val producerA = ProducerA(channel1)
    val producerB = ProducerB(channel2)
    val consumerA = ConsumerA(channel3)
    val consumerB = ConsumerB(channel4)

    launch { 
        producerA.produce()
        channel3.send(channel1.receive())
    }
    launch { 
        producerB.produce()
        channel4.send(channel2.receive())
    }
    launch { 
        consumerA.consume()
        channel5.send(channel3.receive())
    }
    launch {
        consumerB.consume()
        channel5.send(channel4.receive())
    }

    // Simultaneous receive on channel 5 (data from channel 1 and 2 flow to it)
    launch { 
        println("Final: ${channel5.receive()}")
    }
    launch { 
        println("Final: ${channel5.receive()}")
    }
}

class RunChecker429: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}