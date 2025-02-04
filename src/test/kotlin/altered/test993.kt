/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":5,"nChannels":2,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 2 different channels
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
package org.example.altered.test993
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(private val channel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            channel.send(i)
        }
        channel.close()
    }
}

class Consumer(private val channel: Channel<Int>) {
    suspend fun consume() {
        for (i in 1..5) {
            println("Received: ${channel.receive()}")
        }
    }
}

fun firstFunction(ch1: Channel<Int>, ch2: Channel<Int>) = runBlocking {
    launch {
        val producer = Producer(ch1)
        producer.produce()
    }

    launch {
        val consumer = Consumer(ch2)
        consumer.consume()
    }
}

fun secondFunction(ch1: Channel<Int>, ch2: Channel<Int>) = runBlocking {
    launch {
        repeat(5) {
            ch1.send(it * 2)
        }
        ch1.close()
    }

    launch {
        for (i in 1..5) {
            println("Second received: ${ch2.receive()}")
        }
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()

    launch {
        firstFunction(channel1, channel2)
    }

    launch {
        secondFunction(channel1, channel2)
    }
}

class RunChecker993: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}