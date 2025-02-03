/* 
{"deadlock":false,"nFunctions":1,"nCoroutines":1,"nChannels":4,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
- 4 different channels
- 1 different coroutines
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
package org.example.altered.test748
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()

    launch {
        coroutineFunction(channel1, channel2, channel3, channel4)
    }

    channel1.send(1)
    println("Received from channel 4: ${channel4.receive()}")
}

suspend fun coroutineFunction(
    channel1: Channel<Int>,
    channel2: Channel<Int>,
    channel3: Channel<Int>,
    channel4: Channel<Int>
) {
    for (i in 1..10) {
        val received = channel1.receive()
        channel2.send(received * 2)
        val intermediate = channel2.receive()
        channel3.send(intermediate + 1)
        val final = channel3.receive()
        if (i == 10) {
            channel4.send(final * 5)
        } else {
            channel1.send(i + 1)
        }
    }
}

class RunChecker748: RunCheckerBase() {
    override fun block() = main()
}