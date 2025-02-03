/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":6,"nChannels":5,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 5 different channels
- 6 different coroutines
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
package org.example.altered.test21
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>(1)
    val channel2 = Channel<Int>(1)
    val channel3 = Channel<Int>(1)
    val channel4 = Channel<Int>(1)
    val channel5 = Channel<Int>(1)

    launch { producer(channel1, channel2) }
    launch { consumer(channel1, channel3) }
    launch { transfer(channel2, channel3, channel4) }
    launch { producer(channel4, channel5) }
    launch { consumer(channel3, channel5) }
    launch { transfer(channel1, channel5, channel4) }
}

suspend fun producer(channelOut1: Channel<Int>, channelOut2: Channel<Int>) {
    repeat(5) {
        delay(100)
        channelOut1.send(it)
        channelOut2.send(it + 10)
    }
}

suspend fun consumer(channelIn: Channel<Int>, channelOut: Channel<Int>) {
    repeat(5) {
        val received = channelIn.receive()
        delay(100)
        channelOut.send(received)
    }
}

suspend fun transfer(channelIn1: Channel<Int>, channelIn2: Channel<Int>, channelOut: Channel<Int>) {
    repeat(5) {
        val received1 = channelIn1.receive()
        val received2 = channelIn2.receive()
        delay(100)
        channelOut.send(received1 + received2)
    }
}

class RunChecker21: RunCheckerBase() {
    override fun block() = main()
}