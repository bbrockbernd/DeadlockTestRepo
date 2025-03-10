/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":4,"nChannels":1,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 1 different channels
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
package org.example.generated.test952
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer {
    suspend fun produce(channel: Channel<Int>, value: Int) {
        channel.send(value)
    }
}

class Consumer {
    suspend fun consume(channel: Channel<Int>): Int {
        return channel.receive()
    }
}

fun doWorkA(channel: Channel<Int>, producer: Producer) = runBlocking {
    launch {
        producer.produce(channel, 10)
    }
}

fun doWorkB(channel: Channel<Int>, consumer: Consumer): Int = runBlocking {
    var result = 0
    launch {
        result = consumer.consume(channel)
    }
    result
}

fun initiateDeadlock() = runBlocking {
    val channel = Channel<Int>()
    val producer = Producer()
    val consumer = Consumer()

    launch {
        doWorkA(channel, producer)
    }

    launch {
        doWorkB(channel, consumer)
    }
}

fun main(): Unit{
    initiateDeadlock()
}