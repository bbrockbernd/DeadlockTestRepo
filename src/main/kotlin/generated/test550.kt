/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":4,"nChannels":5,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 5 different channels
- 4 different coroutines
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
package org.example.generated.test550
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(private val channel: Channel<String>) {
    suspend fun produce(message: String) {
        channel.send(message)
    }
}

class Consumer(private val channel: Channel<String>) {
    suspend fun consume() {
        for (msg in channel) {
            processMessage(msg)
        }
    }

    private fun processMessage(message: String) {
        println("Consumed: $message")
    }
}

class Mediator(
    private val channel1: Channel<String>,
    private val channel2: Channel<String>,
    private val channel3: Channel<String>
) {
    suspend fun mediate() {
        coroutineScope {
            launch {
                for (msg in channel1) {
                    channel2.send("Modified: $msg")
                }
            }

            launch {
                for (msg in channel2) {
                    channel3.send(msg)
                }
            }
        }
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<String>()
    val channel2 = Channel<String>()
    val channel3 = Channel<String>()
    val producer = Producer(channel1)
    val consumer = Consumer(channel3)
    val mediator = Mediator(channel1, channel2, channel3)

    launch { producer.produce("Hello") }
    launch { producer.produce("World") }
    launch { mediator.mediate() }
    launch { consumer.consume() }
}