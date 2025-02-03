/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":4,"nChannels":5,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 5 different channels
- 4 different coroutines
- 5 different classes

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
package org.example.altered.test55
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class ChannelA(val channel: Channel<Int>)
class ChannelB(val channel: Channel<Int>)
class ChannelC(val channel: Channel<Int>)
class ChannelD(val channel: Channel<Int>)
class ChannelE(val channel: Channel<Int>)

suspend fun functionA(channelA: ChannelA, channelB: ChannelB) {
    channelA.channel.send(1)
    val received = channelB.channel.receive()
    println("functionA received: $received")
}

suspend fun functionB(channelB: ChannelB, channelC: ChannelC) {
    channelB.channel.send(2)
    val received = channelC.channel.receive()
    println("functionB received: $received")
}

suspend fun functionC(channelC: ChannelC, channelD: ChannelD) {
    channelC.channel.send(3)
    val received = channelD.channel.receive()
    println("functionC received: $received")
}

suspend fun functionD(channelD: ChannelD, channelE: ChannelE) {
    channelD.channel.send(4)
    val received = channelE.channel.receive()
    println("functionD received: $received")
}

fun main(): Unit= runBlocking {
    val channelA = ChannelA(Channel<Int>())
    val channelB = ChannelB(Channel<Int>())
    val channelC = ChannelC(Channel<Int>())
    val channelD = ChannelD(Channel<Int>())
    val channelE = ChannelE(Channel<Int>())

    launch {
        functionA(channelA, channelB)
    }

    launch {
        functionB(channelB, channelC)
    }

    launch {
        functionC(channelC, channelD)
    }

    launch {
        functionD(channelD, channelE) // Deadlock will occur here as channelE is not being sent to
    }
}

class RunChecker55: RunCheckerBase() {
    override fun block() = main()
}