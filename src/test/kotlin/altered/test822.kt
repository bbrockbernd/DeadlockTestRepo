/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":5,"nChannels":2,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 2 different channels
- 5 different coroutines
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
package org.example.altered.test822
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(private val channel: Channel<Int>) {
    suspend fun produce() {
        repeat(5) {
            channel.send(it)
        }
    }
}

class Consumer(private val channel: Channel<Int>) {
    suspend fun consume() {
        repeat(5) {
            channel.receive()
        }
    }
}

class Processor(private val inChannel: Channel<Int>, private val outChannel: Channel<Int>) {
    suspend fun process() {
        repeat(5) {
            val value = inChannel.receive()
            outChannel.send(value * 2)
        }
    }
}

fun firstFunction(channelA: Channel<Int>, channelB: Channel<Int>) = runBlocking {
    launch {
        Producer(channelA).produce()
    }
    launch {
        Processor(channelA, channelB).process()
    }
}

fun secondFunction(channelA: Channel<Int>, channelB: Channel<Int>) = runBlocking {
    launch {
        Consumer(channelB).consume()
    }
}

fun thirdFunction(channelA: Channel<Int>, channelB: Channel<Int>) = runBlocking {
    launch {
        Producer(channelA).produce()
        Consumer(channelA).consume()
    }
}

fun main(): Unit{
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()

    firstFunction(channelA, channelB)
    secondFunction(channelA, channelB)
    thirdFunction(channelA, channelB)
}

class RunChecker822: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}