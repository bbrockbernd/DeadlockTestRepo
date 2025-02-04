/* 
{"deadlock":true,"nFunctions":6,"nCoroutines":2,"nChannels":1,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 1 different channels
- 2 different coroutines
- 5 different classes

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
package org.example.altered.test261
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

data class Message(val content: String)

class Producer {
    private val channel = Channel<Message>()

    fun getChannel() = channel

    suspend fun sendMessages() {
        repeat(3) {
            println("Producing Message $it")
            channel.send(Message("Message $it"))
        }
    }
}

class Consumer(val channel: Channel<Message>) {
    suspend fun receiveMessages() {
        repeat(3) {
            val message = channel.receive()
            println("Consumed ${message.content}")
        }
    }
}

class Intermediary {
    suspend fun passMessages(producer: Producer, consumer: Consumer) {
        coroutineScope {
            launch {
                producer.sendMessages()
            }
            launch {
                consumer.receiveMessages()
            }
        }
    }
}

fun runBlockingExample1(intermediary: Intermediary, producer: Producer, consumer: Consumer) = runBlocking {
    intermediary.passMessages(producer, consumer)
}

fun runBlockingExample2(intermediary: Intermediary, producer: Producer, consumer: Consumer) = runBlocking {
    intermediary.passMessages(producer, consumer)
}

fun main(): Unit {
    val producer = Producer()
    val consumer = Consumer(producer.getChannel())
    val intermediary1 = Intermediary()
    val intermediary2 = Intermediary()

    // One of these launch setups will cause a deadlock
    runBlockingExample1(intermediary1, producer, consumer)
    runBlockingExample2(intermediary2, producer, consumer)
}

class RunChecker261: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}