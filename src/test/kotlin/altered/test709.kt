/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":2,"nChannels":3,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 3 different channels
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
package org.example.altered.test709
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun sendNumbers(channel: Channel<Int>) = runBlocking {
    launch {
        for (x in 1..5) channel.send(x)
        channel.close()
    }
}

fun receiveAndTransform(channelIn: Channel<Int>, channelOut: Channel<String>) = runBlocking {
    launch {
        for (y in channelIn) {
            channelOut.send("Received: $y")
        }
        channelOut.close()
    }
}

fun processTransformed(channel: Channel<String>) = runBlocking {
    launch {
        for (z in channel) {
            println(z)
        }
    }
}

fun initFirstChannel(): Channel<Int> {
    return Channel()
}

fun initSecondChannel(): Channel<String> {
    return Channel()
}

fun main(): Unit= runBlocking {
    val channel1 = initFirstChannel()
    val channel2 = initSecondChannel()

    sendNumbers(channel1)
    receiveAndTransform(channel1, channel2)
    processTransformed(channel2)
}

class RunChecker709: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}