/* 
{"deadlock":true,"nFunctions":1,"nCoroutines":4,"nChannels":4,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
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
package org.example.altered.test526
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class Sender(val channel: Channel<Int>) {
    suspend fun send(value: Int) {
        channel.send(value)
    }
}

class Receiver(val channel: Channel<Int>) {
    suspend fun receive(): Int {
        return channel.receive()
    }
}

fun startCoroutines() = runBlocking {
    // Create channels
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()

    // Create sender and receiver instances
    val sender1 = Sender(channel1)
    val receiver1 = Receiver(channel2)
    val sender2 = Sender(channel3)
    val receiver2 = Receiver(channel4)

    // Start coroutines
    launch {
        val received1 = receiver1.receive()
        sender1.send(received1)
    }

    launch {
        val received2 = receiver2.receive()
        sender2.send(received2)
    }

    launch {
        val received3 = receiver1.receive()
        sender2.send(received3)
    }

    launch {
        val received4 = receiver2.receive()
        sender1.send(received4)
    }
}

fun main(): Unit{
    startCoroutines()
}

class RunChecker526: RunCheckerBase() {
    override fun block() = main()
}