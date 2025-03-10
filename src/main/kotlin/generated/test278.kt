/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":4,"nChannels":7,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 7 different channels
- 4 different coroutines
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
package org.example.generated.test278
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class Producer {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()

    suspend fun produceToChannels() {
        for (i in 1..5) {
            channel1.send(i)
            channel2.send(i)
            channel3.send(i)
        }
        channel1.close()
        channel2.close()
        channel3.close()
    }
}

class Consumer {
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()
    val channel6 = Channel<Int>()

    suspend fun consumeFromChannels(channel: Channel<Int>) {
        for (i in channel) {
            when(channel) {
                channel4 -> println("Consumer4 received: $i")
                channel5 -> println("Consumer5 received: $i")
                channel6 -> println("Consumer6 received: $i")
            }
        }
    }
}

fun filterData(channelIn: Channel<Int>, channelOut: Channel<Int>) = runBlocking {
    for (i in channelIn) {
        if (i % 2 == 0) {
            channelOut.send(i)
        }
    }
    channelOut.close()
}

fun processAndPrintData(channel: Channel<Int>) = runBlocking {
    for (i in channel) {
        println("Data processed: ${i * 2}")
    }
}

fun main(): Unit= runBlocking {
    val producer = Producer()
    val consumer = Consumer()
    val channel7 = Channel<Int>()

    launch { producer.produceToChannels() }
    launch { filterData(producer.channel1, consumer.channel4) }
    launch { filterData(producer.channel2, consumer.channel5) }
    launch { filterData(producer.channel3, consumer.channel6) }

    launch { consumer.consumeFromChannels(consumer.channel4) }
    launch { consumer.consumeFromChannels(consumer.channel5) }
    launch { consumer.consumeFromChannels(consumer.channel6) }

    launch { processAndPrintData(channel7) }
}