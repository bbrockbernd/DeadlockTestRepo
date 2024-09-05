/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":3,"nChannels":2,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 2 different channels
- 3 different coroutines
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
package org.example.generated.test346
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(name: String) {
    val producerName = name
    suspend fun produceItems(channel: Channel<String>) {
        repeat(5) {
            channel.send("$producerName item $it")
        }
    }
}

class Consumer(name: String) {
    val consumerName = name
    suspend fun consumeItems(channel: Channel<String>) {
        repeat(5) {
            val item = channel.receive()
            println("$consumerName received: $item")
        }
    }
}

class Mediator(name: String) {
    val mediatorName = name
    suspend fun mediate(produceChannel: Channel<String>, consumeChannel: Channel<String>) {
        repeat(5) {
            val item = produceChannel.receive()
            println("$mediatorName mediating: $item")
            consumeChannel.send(item)
        }
    }
}

class Coordinator {
    fun coordinate() {
        val produceChannel = Channel<String>()
        val consumeChannel = Channel<String>()

        runBlocking {
            launch { producer.produceItems(produceChannel) }
            launch { mediator.mediate(produceChannel, consumeChannel) }
            launch { consumer.consumeItems(consumeChannel) }
        }
    }

    private val producer = Producer("Producer1")
    private val mediator = Mediator("Mediator1")
    private val consumer = Consumer("Consumer1")
}

fun main(): Unit{
    Coordinator().coordinate()
}