/* 
{"deadlock":false,"nFunctions":6,"nCoroutines":2,"nChannels":1,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 1 different channels
- 2 different coroutines
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
package org.example.generated.test249
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(val channel: Channel<Int>) {
    fun produce() = CoroutineScope(Dispatchers.Default).launch {
        for (i in 1..5) {
            channel.send(i)
            delay(100) // simulate work
        }
        channel.close()
    }
}

class Consumer(val channel: Channel<Int>) {
    fun consume() = CoroutineScope(Dispatchers.Default).launch {
        for (value in channel) {
            println("Consumed: $value")
            delay(150) // simulate work
        }
    }
}

suspend fun runProducer(producer: Producer) {
    producer.produce().join()
}

suspend fun runConsumer(consumer: Consumer) {
    consumer.consume().join()
}

fun startProductionAndConsumption(channel: Channel<Int>) = runBlocking {
    val producer = Producer(channel)
    val consumer = Consumer(channel)

    coroutineScope {
        launch { runProducer(producer) }
        launch { runConsumer(consumer) }
    }
}

fun main(): Unit {
    val channel = Channel<Int>()
    startProductionAndConsumption(channel)
}