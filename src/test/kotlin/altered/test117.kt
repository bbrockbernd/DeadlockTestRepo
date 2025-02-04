/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":3,"nChannels":2,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 2 different channels
- 3 different coroutines
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
package org.example.altered.test117
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(private val channel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            channel.send(i)
        }
    }
}

class Consumer(private val channel: Channel<Int>) {
    suspend fun consume() {
        for (i in 1..5) {
            println(channel.receive())
        }
    }
}

class Mediator(private val channel1: Channel<Int>, private val channel2: Channel<Int>) {
    suspend fun mediate() {
        repeat(5) {
            val value = channel1.receive()
            channel2.send(value)
        }
    }
}

fun runProducer(producer: Producer) = runBlocking {
    launch { producer.produce() }
}

fun runConsumer(consumer: Consumer) = runBlocking {
    launch { consumer.consume() }
}

fun runMediator(mediator: Mediator) = runBlocking {
    launch { mediator.mediate() }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()

    val producer = Producer(channel1)
    val consumer = Consumer(channel2)
    val mediator = Mediator(channel1, channel2)

    launch { runProducer(producer) }
    launch { runMediator(mediator) }
    launch { runConsumer(consumer) }
    
    delay(1000)
}

class RunChecker117: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}