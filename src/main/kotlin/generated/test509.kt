/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":1,"nChannels":2,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 2 different channels
- 1 different coroutines
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
package org.example.generated.test509
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()

    suspend fun produceItems() {
        for (i in 1..5) {
            channel1.send(i)
        }
        channel1.close()
    }

    suspend fun transferItems() {
        for (item in channel1) {
            channel2.send(item * 2)
        }
        channel2.close()
    }
}

class Consumer {
    suspend fun consumeItems(channel: Channel<Int>): Int {
        var sum = 0
        for (item in channel) {
            sum += item
        }
        return sum
    }
}

suspend fun producerConsumerLogic(producer: Producer, consumer: Consumer): Int {
    return consumer.consumeItems(producer.channel2)
}

fun main(): Unit= runBlocking {
    val producer = Producer()
    val consumer = Consumer()

    launch {
        producer.produceItems()
    }.invokeOnCompletion {
        launch {
            producer.transferItems()
        }.invokeOnCompletion {
            launch {
                val result = producerConsumerLogic(producer, consumer)
                println(result)
            }
        }
    }
}