/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":5,"nChannels":1,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 1 different channels
- 5 different coroutines
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
package org.example.generated.test915
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(private val channel: Channel<Int>) {
    suspend fun produce() {
        repeat(5) {
            channel.send(it)
        }
        channel.close()
    }
}

class Consumer(private val channel: Channel<Int>) {
    suspend fun consume() {
        for (item in channel) {
            processData(item)
        }
    }

    private fun processData(data: Int) {
        println(data)
    }
}

suspend fun startProducer(producer: Producer) = coroutineScope {
    launch {
        producer.produce()
    }
}

suspend fun startConsumer(consumer: Consumer) = coroutineScope {
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
    delay(1000)  // Give some time for coroutines to finish
}