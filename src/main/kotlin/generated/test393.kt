/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":1,"nChannels":3,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 3 different channels
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
package org.example.generated.test393
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(private val channel: Channel<Int>) {
    suspend fun produceData() {
        for (i in 1..5) {
            channel.send(i)
        }
        channel.close()
    }
}

class Consumer(private val channel: Channel<Int>) {
    suspend fun consumeData() {
        for (value in channel) {
            println("Consumed: $value")
        }
    }
}

fun provideProducer(channel: Channel<Int>): Producer {
    return Producer(channel)
}

fun provideConsumer(channel: Channel<Int>): Consumer {
    return Consumer(channel)
}

suspend fun processChannels(ch1: Channel<Int>, ch2: Channel<Int>, ch3: Channel<Int>) {
    coroutineScope {
        val producer = provideProducer(ch1)
        val consumer = provideConsumer(ch1)
        launch { producer.produceData() }
        launch { consumer.consumeData() }
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    processChannels(channel1, channel2, channel3)
}