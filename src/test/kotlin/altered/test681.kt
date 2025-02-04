/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":1,"nChannels":3,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 3 different channels
- 1 different coroutines
- 3 different classes

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
package org.example.altered.test681
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ChannelA(val channel: Channel<Int>)
class ChannelB(val channel: Channel<Int>)
class ChannelC(val channel: Channel<Int>)

fun function1(channelA: ChannelA, channelB: ChannelB) = runBlocking {
    launch {
        channelA.channel.send(1)
        channelB.channel.receive()
    }
}

fun function2(channelB: ChannelB, channelC: ChannelC) = runBlocking {
    launch {
        channelB.channel.send(2)
        channelC.channel.receive()
    }
}

fun function3(channelC: ChannelC, channelA: ChannelA) = runBlocking {
    launch {
        channelC.channel.send(3)
        channelA.channel.receive()
    }
}

fun main(): Unit= runBlocking {
    val channelA = ChannelA(Channel<Int>())
    val channelB = ChannelB(Channel<Int>())
    val channelC = ChannelC(Channel<Int>())

    function1(channelA, channelB)
    function2(channelB, channelC)
    function3(channelC, channelA)
}

class RunChecker681: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}