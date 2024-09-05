/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":5,"nChannels":5,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 5 different channels
- 5 different coroutines
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
package org.example.generated.test987
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>(capacity = 1)
    val channel2 = Channel<Int>(capacity = 1)
    val channel3 = Channel<Int>(capacity = 1)
    val channel4 = Channel<Int>(capacity = 1)
    val channel5 = Channel<Int>(capacity = 1)

    launch { function1(channel1, channel2) }
    launch { function2(channel2, channel3) }
    launch { function3(channel3, channel4) }
    launch { function4(channel4, channel5) }
    launch { function5(channel5, channel1) }
}

suspend fun function1(channelIn: Channel<Int>, channelOut: Channel<Int>) {
    val received = channelIn.receive()
    val processed = received + 1
    channelOut.send(processed)
}

suspend fun function2(channelIn: Channel<Int>, channelOut: Channel<Int>) {
    val received = channelIn.receive()
    val processed = received * 2
    channelOut.send(processed)
}

suspend fun function3(channelIn: Channel<Int>, channelOut: Channel<Int>) {
    val received = channelIn.receive()
    val processed = received - 1
    channelOut.send(processed)
}

suspend fun function4(channelIn: Channel<Int>, channelOut: Channel<Int>) {
    val received = channelIn.receive()
    val processed = received / 2
    channelOut.send(processed)
}

suspend fun function5(channelIn: Channel<Int>, channelOut: Channel<Int>) {
    val received = channelIn.receive()
    val processed = received + 10
    channelOut.send(processed)
}