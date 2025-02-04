/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":3,"nChannels":4,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 4 different channels
- 3 different coroutines
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
package org.example.altered.test912
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ChannelA(val channel: Channel<Int>)
class ChannelB(val channel: Channel<Int>)
class ChannelC(val channel: Channel<Int>)

fun first(channelA: ChannelA, channelB: ChannelB) {
    runBlocking {
        val jobA = launch {
            channelA.channel.send(1)
            channelB.channel.receive()
        }
        jobA.start()
    }
}

fun second(channelB: ChannelB, channelC: ChannelC) {
    runBlocking {
        val jobB = launch {
            channelB.channel.send(2)
            channelC.channel.receive()
        }
        jobB.start()
    }
}

fun third(channelC: ChannelC, channelA: ChannelA) {
    runBlocking {
        val jobC = launch {
            channelC.channel.send(3)
            channelA.channel.receive()
        }
        jobC.start()
    }
}

class ChannelD(val channel: Channel<Int>)
fun fourth(channelD: ChannelD, channelA: ChannelA) {
    runBlocking {
        val jobD = launch {
            channelD.channel.send(4)
            channelA.channel.receive()
        }
        jobD.start()
    }
}

fun main(): Unit{
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()
    val channelC = Channel<Int>()
    val channelD = Channel<Int>()
    
    val chanA = ChannelA(channelA)
    val chanB = ChannelB(channelB)
    val chanC = ChannelC(channelC)
    val chanD = ChannelD(channelD)
    
    first(chanA, chanB)
    second(chanB, chanC)
    third(chanC, chanA)
    fourth(chanD, chanA)
}

class RunChecker912: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}