/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":7,"nChannels":1,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 1 different channels
- 7 different coroutines
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
package org.example.altered.test374
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(private val channel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            channel.send(i)
        }
    }
}

class Consumer(private val channel: Channel<Int>) {
    suspend fun consume() {
        for (i in 1..5) {
            channel.receive()
        }
    }
}

fun produceData(channel: Channel<Int>) = runBlocking {
    val producer = Producer(channel)
    producer.produce()
}

fun consumeData(channel: Channel<Int>) = runBlocking {
    val consumer = Consumer(channel)
    consumer.consume()
}

fun main(): Unit = runBlocking {
    val channel = Channel<Int>()

    launch {
        produceData(channel)
    }

    launch {
        consumeData(channel)
    }

    launch {
        produceData(channel)
    }

    launch {
        consumeData(channel)
    }

    launch {
        produceData(channel)
    }

    launch {
        consumeData(channel)
    }

    launch {
        produceData(channel)
    }
}

class RunChecker374: RunCheckerBase() {
    override fun block() = main()
}