/* 
{"deadlock":true,"nFunctions":1,"nCoroutines":3,"nChannels":5,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
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
package org.example.generated.test559
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(val channel1: Channel<Int>, val channel2: Channel<Int>) {
    suspend fun produce() {
        channel1.send(1)
        delay(100)
        channel2.send(2)
    }
}

class Consumer(val channel3: Channel<Int>, val channel4: Channel<Int>, val channel5: Channel<Int>) {
    suspend fun consume() {
        val value1 = channel3.receive()
        channel4.send(value1)
        val value2 = channel5.receive()
        channel3.send(value2)
    }
}

fun process(channel1: Channel<Int>, channel2: Channel<Int>, channel3: Channel<Int>, channel4: Channel<Int>, channel5: Channel<Int>) = runBlocking {
    val producer = Producer(channel1, channel2)
    val consumer = Consumer(channel3, channel4, channel5)

    launch {
        producer.produce()
    }

    launch {
        consumer.consume()
    }

    launch {
        val value1 = channel1.receive()
        channel3.send(value1)
        val value2 = channel2.receive()
        channel5.send(value2)
    }
}

fun main(): Unit{
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()

    process(channel1, channel2, channel3, channel4, channel5)
}