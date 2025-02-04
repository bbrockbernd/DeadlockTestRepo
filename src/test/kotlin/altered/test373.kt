/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":3,"nChannels":2,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
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
package org.example.altered.test373
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(val channel: Channel<Int>) {

    suspend fun produce() {
        for (i in 1..5) {
            channel.send(i)
            println("Produced: $i")
        }
        channel.close()
    }
}

class Consumer(val channel: Channel<Int>) {

    suspend fun consume() {
        for (i in channel) {
            println("Consumed: $i")
        }
    }
}

class Mediator(val producer: Producer, val consumer: Consumer) {

    suspend fun mediate() = coroutineScope {
        val channelA = Channel<Int>()
        val channelB = Channel<Int>()

        launch {
            producer.produce()
            for (item in channelA) { 
                channelB.send(item) 
            }
            channelB.close()
        }

        launch {
            for (item in channelB) {
                consumer.consume()
                channelA.send(item)
            }
            channelA.close()
        }
    }
}

fun main(): Unit = runBlocking {
    val channel = Channel<Int>()
    val producer = Producer(channel)
    val consumer = Consumer(channel)
    val mediator = Mediator(producer, consumer)
    
    launch {
        mediator.mediate()
    }
}

class RunChecker373: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}