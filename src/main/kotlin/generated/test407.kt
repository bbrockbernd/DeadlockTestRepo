/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":2,"nChannels":5,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 5 different channels
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
package org.example.generated.test407
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(val channel1: Channel<Int>, val channel2: Channel<Int>) {
    suspend fun produce() {
        channel1.send(1)
        channel2.send(2)
    }
    
    suspend fun intermediate() {
        val value = channel1.receive()
        channel2.send(value)
    }
}

class Consumer(val channel3: Channel<Int>, val channel4: Channel<Int>, val channel5: Channel<Int>) {
    suspend fun consume() {
        val value1 = channel3.receive()
        val value2 = channel4.receive()
        channel5.send(value1 + value2)
    }
    
    suspend fun intermediate() {
        val value = channel4.receive()
        channel4.send(value)
    }
}

suspend fun process() {
    val channel1 = Channel<Int>(1)
    val channel2 = Channel<Int>(1)
    val channel3 = Channel<Int>(1)
    val channel4 = Channel<Int>(1)
    val channel5 = Channel<Int>(1)

    val producer = Producer(channel1, channel2)
    val consumer = Consumer(channel3, channel4, channel5)
    
    coroutineScope {
        launch {
            producer.produce()
        }
        launch {
            producer.intermediate()
        }
        launch {
            consumer.consume()
        }
        launch {
            consumer.intermediate()
        }
    }
}

fun main(): Unit = runBlocking {
    process()
}