/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":4,"nChannels":4,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 4 different channels
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
package org.example.altered.test798
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer1(private val channel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            channel.send(i)
        }
    }
}

class Producer2(private val channel: Channel<Int>) {
    suspend fun produce() {
        for (i in 6..10) {
            channel.send(i)
        }
    }
}

suspend fun consumer1(inputChannel: Channel<Int>, outputChannel: Channel<Int>) {
    for (i in 1..5) {
        val value = inputChannel.receive()
        outputChannel.send(value)
    }
}

suspend fun consumer2(inputChannel: Channel<Int>, outputChannel: Channel<Int>) {
    for (i in 6..10) {
        val value = inputChannel.receive()
        outputChannel.send(value)
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()

    val producer1 = Producer1(channel1)
    val producer2 = Producer2(channel1)

    launch {
        producer1.produce()
    }

    launch {
        producer2.produce()
    }

    launch {
        consumer1(channel1, channel3)
    }

    launch {
        consumer2(channel3, channel4)
    }

    // Introducing deadlock by waiting forever
    channel2.receive()
}

class RunChecker798: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}