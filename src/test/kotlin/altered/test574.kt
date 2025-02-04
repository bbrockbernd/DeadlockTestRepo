/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":5,"nChannels":2,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
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
package org.example.altered.test574
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class Producer(private val channel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            channel.send(i)
        }
    }
}

class Consumer(private val channel: Channel<Int>) {
    suspend fun consume() {
        repeat(5) {
            println(channel.receive())
        }
    }
}

fun operateChannels(channel1: Channel<Int>, channel2: Channel<Int>) = runBlocking {
    launch {
        for (i in 1..5) {
            channel1.send(i)
            channel2.send(channel1.receive())
        }
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()

    val producer = Producer(channel1)
    val consumer = Consumer(channel2)

    launch {
        producer.produce()
    }

    launch {
        consumer.consume()
    }

    operateChannels(channel1, channel2)

    launch {
        channel1.receive()
    }
    
    launch {
        channel2.receive()
    }
}

class RunChecker574: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}