/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":2,"nChannels":2,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 2 different channels
- 2 different coroutines
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
package org.example.altered.test546
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun sendToChannelA(channelA: Channel<Int>) {
    runBlocking {
        channelA.send(1)
    }
}

fun receiveFromChannelAAndSendToChannelB(channelA: Channel<Int>, channelB: Channel<Int>) {
    runBlocking {
        val received = channelA.receive()
        channelB.send(received)
    }
}

fun receiveFromChannelB(channelB: Channel<Int>): Int {
    return runBlocking {
        channelB.receive()
    }
}

fun main(): Unit{
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()

    runBlocking {
        launch {
            sendToChannelA(channelA)
        }
        launch {
            receiveFromChannelAAndSendToChannelB(channelA, channelB)
        }
        val result = receiveFromChannelB(channelB)
        println(result)
    }
}

class RunChecker546: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}