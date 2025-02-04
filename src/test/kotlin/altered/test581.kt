/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":5,"nChannels":2,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
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
package org.example.altered.test581
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class Producer1 {
    suspend fun produce(channel: Channel<Int>) {
        repeat(5) {
            channel.send(it)
        }
    }
}

class Producer2 {
    suspend fun produce(channel: Channel<Int>) {
        repeat(5) {
            channel.send(it)
        }
    }
}

suspend fun consume(channel1: Channel<Int>, channel2: Channel<Int>) {
    for (i in 1..5) {
        val value = channel1.receive()
        channel2.send(value * 2)
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()

    val producer1 = Producer1()
    val producer2 = Producer2()

    launch {
        producer1.produce(channel1)
    }

    launch {
        producer2.produce(channel1)
    }

    launch {
        consume(channel1, channel2)
    }

    launch {
        for (i in 1..5) {
            val value = channel2.receive()
            println("Received: $value")
        }
    }

    launch {
        // Potential deadlock caused by this coroutine
        channel1.receive()
    }
}

class RunChecker581: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}