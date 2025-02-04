/* 
{"deadlock":false,"nFunctions":7,"nCoroutines":1,"nChannels":1,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 1 different channels
- 1 different coroutines
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
package org.example.altered.test138
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(val channel: Channel<String>) {
    suspend fun produce() {
        val message = createMessage()
        channel.send(message)
    }

    private fun createMessage(): String {
        return "Hello from Producer!"
    }
}

class Consumer(val channel: Channel<String>) {
    suspend fun consume() {
        val message = channel.receive()
        printMessage(message)
    }

    private fun printMessage(message: String) {
        println("Consumer received: $message")
    }
}

class Manager(val producer: Producer, val consumer: Consumer) {
    suspend fun manage() {
        producer.produce()
        consumer.consume()
    }
}

fun runProducerCoroutine(producer: Producer) {
    GlobalScope.launch {
        producer.produce()
    }
}

fun runConsumerCoroutine(consumer: Consumer) {
    GlobalScope.launch {
        consumer.consume()
    }
}

fun runManagerCoroutine(manager: Manager) {
    GlobalScope.launch {
        manager.manage()
    }
}

fun main(): Unit= runBlocking {
    val channel = Channel<String>()
    val producer = Producer(channel)
    val consumer = Consumer(channel)
    val manager = Manager(producer, consumer)

    runProducerCoroutine(producer)
    runConsumerCoroutine(consumer)
    runManagerCoroutine(manager)

    delay(1000) // Allow some time for coroutines to complete
}

class RunChecker138: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}