/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":1,"nChannels":3,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
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
package org.example.altered.test687
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(private val channel1: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            channel1.send(i)
        }
        channel1.close()
    }
}

class Consumer(private val channel2: Channel<Int>, private val channel3: Channel<String>) {
    suspend fun consume() {
        for (i in channel2) {
            channel3.send("Received: $i")
        }
        channel3.close()
    }
}

suspend fun mediator(channel1: Channel<Int>, channel2: Channel<Int>) {
    for (i in channel1) {
        channel2.send(i * 2)
    }
    channel2.close()
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<String>()

    val producer = Producer(channel1)
    val consumer = Consumer(channel2, channel3)

    launch {
        producer.produce()
    }

    launch {
        mediator(channel1, channel2)
    }

    launch {
        consumer.consume()
    }

    for (message in channel3) {
        println(message)
    }
}

class RunChecker687: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}