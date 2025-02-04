/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":3,"nChannels":2,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 2 different channels
- 3 different coroutines
- 0 different classes

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
package org.example.altered.test983
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun producer1(channel: Channel<Int>) = runBlocking {
    launch {
        channel.send(1)
    }
}

fun producer2(channel: Channel<Int>) = runBlocking {
    launch {
        channel.send(2)
    }
}

fun consumer1(channel: Channel<Int>) = runBlocking {
    launch {
        channel.receive()
    }
}

fun createPipeline(channel1: Channel<Int>, channel2: Channel<Int>) = runBlocking {
    launch {
        val value = channel1.receive()
        channel2.send(value)
    }
}

fun initiateDeadlock(channel1: Channel<Int>, channel2: Channel<Int>) = runBlocking {
    launch {
        producer1(channel1)
        producer2(channel2)
        createPipeline(channel1, channel2)
        consumer1(channel2)
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()

    initiateDeadlock(channel1, channel2)
}

class RunChecker983: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}