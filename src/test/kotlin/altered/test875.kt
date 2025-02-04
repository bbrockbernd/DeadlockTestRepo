/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":2,"nChannels":3,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 3 different channels
- 2 different coroutines
- 3 different classes

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
package org.example.altered.test875
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

class Consumer(private val channel1: Channel<Int>, private val channel2: Channel<Int>, private val finalChannel: Channel<Int>) {
    suspend fun consume() {
        var received1 = channel1.receive()
        var received2 = channel2.receive()

        finalChannel.send(received1 + received2)
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>(5)
    val channel2 = Channel<Int>(5)
    val finalChannel = Channel<Int>(1)

    val producer1 = Producer1(channel1)
    val producer2 = Producer2(channel2)
    val consumer = Consumer(channel1, channel2, finalChannel)

    launch {
        producer1.produce()
    }

    launch {
        producer2.produce()
    }

    launch {
        consumer.consume()
        println(finalChannel.receive())
    }
}

class RunChecker875: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}