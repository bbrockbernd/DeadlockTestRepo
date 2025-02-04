/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":8,"nChannels":6,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 6 different channels
- 8 different coroutines
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
package org.example.altered.test151
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>(1)
    val channel2 = Channel<Int>(1)
    val channel3 = Channel<Int>(1)
    val channel4 = Channel<Int>(1)
    val channel5 = Channel<Int>(1)
    val channel6 = Channel<Int>(1)

    launch { functionA(channel1, channel2) }
    launch { functionA(channel2, channel3) }
    launch { functionB(channel3, channel4) }
    launch { functionB(channel4, channel5) }
    launch { functionC(channel5, channel6) }
    launch { functionC(channel6, channel1) } 
    launch { functionA(channel1, channel4) }
    launch { functionB(channel2, channel5) }
}

suspend fun functionA(inChannel: Channel<Int>, outChannel: Channel<Int>) {
    val value = inChannel.receive()
    outChannel.send(value)
}

suspend fun functionB(inChannel1: Channel<Int>, outChannel: Channel<Int>) {
    val value = inChannel1.receive()
    outChannel.send(value + 1)
}

suspend fun functionC(inChannel: Channel<Int>, outChannel: Channel<Int>) {
    val value = inChannel.receive()
    outChannel.send(value * 2)
}

class RunChecker151: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}