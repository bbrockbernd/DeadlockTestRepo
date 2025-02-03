/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":1,"nChannels":8,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 8 different channels
- 1 different coroutines
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
package org.example.altered.test464
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.launch

class ChannelManager {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()
    val channel6 = Channel<Int>()
    val channel7 = Channel<Int>()
    val channel8 = Channel<Int>()
}

fun function1(channelManager: ChannelManager) = runBlocking {
    launch {
        channelManager.channel1.send(1)
        channelManager.channel2.send(2)
    }
    launch {
        println(channelManager.channel3.receive())
        println(channelManager.channel4.receive())
    }
}

fun function2(channelManager: ChannelManager) = runBlocking {
    launch {
        println(channelManager.channel5.receive())
        println(channelManager.channel6.receive())
    }
}

fun function3(channelManager: ChannelManager) = runBlocking {
    launch {
        channelManager.channel3.send(3)
        channelManager.channel4.send(4)
    }
    launch {
        channelManager.channel5.send(5)
        channelManager.channel6.send(6)
    }
}

fun function4(channelManager: ChannelManager) = runBlocking {
    launch {
        println(channelManager.channel1.receive())
        println(channelManager.channel2.receive())
    }
    launch {
        channelManager.channel7.send(7)
        channelManager.channel8.send(8)
    }
    launch {
        println(channelManager.channel7.receive())
        println(channelManager.channel8.receive())
    }
}

fun main(): Unit= runBlocking {
    val channelManager = ChannelManager()
    function1(channelManager)
    function2(channelManager)
    function3(channelManager)
    function4(channelManager)
}

class RunChecker464: RunCheckerBase() {
    override fun block() = main()
}