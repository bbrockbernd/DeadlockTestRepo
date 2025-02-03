/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":2,"nChannels":2,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
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
package org.example.altered.test653
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

suspend fun sendToFirstChannel(channel1: Channel<Int>) {
    for (i in 1..5) {
        channel1.send(i)
    }
    channel1.close()
}

suspend fun processFromFirstAndSendToSecond(channel1: Channel<Int>, channel2: Channel<Int>) {
    for (element in channel1) {
        channel2.send(element * element)
    }
    channel2.close()
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()

    launch {
        sendToFirstChannel(channel1)
    }

    launch {
        processFromFirstAndSendToSecond(channel1, channel2)
    }

    for (result in channel2) {
        println(result)
    }
}

class RunChecker653: RunCheckerBase() {
    override fun block() = main()
}