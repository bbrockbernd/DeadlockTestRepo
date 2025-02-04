/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":2,"nChannels":8,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 8 different channels
- 2 different coroutines
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
package org.example.altered.test309
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class Product(val id: Int, val name: String)

class Producer {
    suspend fun produce(channel: SendChannel<Product>, id: Int, name: String) {
        val product = Product(id, name)
        channel.send(product)
    }
}

class Consumer {
    suspend fun consume(channel: ReceiveChannel<Product>, outputChannel: SendChannel<String>) {
        val product = channel.receive()
        outputChannel.send("Consumed: ${product.name}")
    }
}

fun createChannels(): List<Channel<*>> {
    return listOf(
        Channel<Product>(), Channel<Product>(), Channel<Product>(), Channel<Product>(),
        Channel<String>(), Channel<String>(), Channel<String>(), Channel<String>()
    )
}

fun runProducers(producer: Producer, channels: List<Channel<*>>) = runBlocking {
    launch {
        producer.produce(channels[0] as SendChannel<Product>, 1, "ProductA")
        producer.produce(channels[1] as SendChannel<Product>, 2, "ProductB")
        producer.produce(channels[2] as SendChannel<Product>, 3, "ProductC")
        producer.produce(channels[3] as SendChannel<Product>, 4, "ProductD")
    }
}

fun runConsumers(consumer: Consumer, channels: List<Channel<*>>) = runBlocking {
    launch {
        consumer.consume(channels[0] as ReceiveChannel<Product>, channels[4] as SendChannel<String>)
        consumer.consume(channels[1] as ReceiveChannel<Product>, channels[5] as SendChannel<String>)
        consumer.consume(channels[2] as ReceiveChannel<Product>, channels[6] as SendChannel<String>)
        consumer.consume(channels[3] as ReceiveChannel<Product>, channels[7] as SendChannel<String>)
    }
}

fun main(): Unit{
    val channels = createChannels()
    val producer = Producer()
    val consumer = Consumer()

    runProducers(producer, channels)
    runConsumers(consumer, channels)
}

class RunChecker309: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}