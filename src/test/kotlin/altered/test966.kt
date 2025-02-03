/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":3,"nChannels":5,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 5 different channels
- 3 different coroutines
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
package org.example.altered.test966
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer {
    val channel1 = Channel<Int>(5)
    val channel2 = Channel<Int>(5)

    suspend fun produce() {
        for (i in 1..10) {
            channel1.send(i)
            channel2.send(i)
        }
    }
}

class Consumer {
    val channel3 = Channel<Int>(5)
    val channel4 = Channel<Int>(5)

    suspend fun consume(channel: Channel<Int>) {
        for (i in 1..10) {
            val item = channel.receive()
            channel3.send(item)
            channel4.send(item)
        }
    }
}

suspend fun process(producer: Producer, consumer: Consumer, channel5: Channel<Int>) {
    coroutineScope {
        launch {
            producer.produce()
        }
        launch {
            consumer.consume(producer.channel1)
        }
        launch {
            channel5.send(consumer.channel4.receive())
        }
    }
}

fun main(): Unit= runBlocking {
    val producer = Producer()
    val consumer = Consumer()
    val channel5 = Channel<Int>(5)

    launch {
        process(producer, consumer, channel5)
    }

    launch {
        consumer.consume(producer.channel2)
    }
}

class RunChecker966: RunCheckerBase() {
    override fun block() = main()
}