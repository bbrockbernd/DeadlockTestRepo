/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":2,"nChannels":1,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
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
package org.example.generated.test970
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(val channel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            channel.send(i)
            delay(100)
        }
        println("Produced all items")
    }
}

class Consumer(val channel: Channel<Int>) {
    suspend fun consume() {
        while (true) {
            val item = channel.receive()
            println("Consumed item: $item")
            delay(200)
        }
    }
}

fun startProducer(producer: Producer) = runBlocking {
    launch {
        producer.produce()
    }
}

fun startConsumer(consumer: Consumer) = runBlocking {
    launch {
        consumer.consume()
    }
}

fun main(): Unit= runBlocking {
    val channel = Channel<Int>()
    val producer = Producer(channel)
    val consumer = Consumer(channel)

    startProducer(producer)
    startConsumer(consumer)

    // Deadlock situation: both coroutines are waiting on each other
    delay(5000)
    println("End of main")
}