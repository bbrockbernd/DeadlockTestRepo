/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":2,"nChannels":7,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 7 different channels
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
package org.example.generated.test467
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ChannelHandler {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()
    val channel6 = Channel<Int>()
    val channel7 = Channel<Int>()
}

suspend fun producer(handler: ChannelHandler) {
    handler.channel1.send(1)
    handler.channel2.send(2)
    handler.channel3.send(3)
    handler.channel4.send(4)
    handler.channel5.send(5)
    handler.channel6.send(6)
    handler.channel7.send(7)
}

suspend fun consumer(handler: ChannelHandler) {
    println(handler.channel1.receive())
    println(handler.channel2.receive())
    println(handler.channel3.receive())
    println(handler.channel4.receive())
    println(handler.channel5.receive())
    println(handler.channel6.receive())
    println(handler.channel7.receive())
}

fun main(): Unit= runBlocking {
    val handler = ChannelHandler()

    launch { producer(handler) }
    launch { consumer(handler) }
}