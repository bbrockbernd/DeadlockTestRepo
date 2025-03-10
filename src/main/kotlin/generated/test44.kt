/* 
{"deadlock":true,"nFunctions":2,"nCoroutines":2,"nChannels":4,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 4 different channels
- 2 different coroutines
- 1 different classes

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
package org.example.generated.test44
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ChannelHandler {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()

    suspend fun sendToChannel1(value: Int) {
        channel1.send(value)
        channel2.receive()
    }

    suspend fun sendToChannel2(value: Int) {
        channel2.send(value)
        channel1.receive()
    }
}

fun producer(handler: ChannelHandler) = runBlocking {
    launch {
        handler.sendToChannel1(10)
    }
    launch {
        handler.sendToChannel2(20)
    }
}

fun main(): Unit{
    runBlocking {
        val handler = ChannelHandler()
        producer(handler)
    }
}