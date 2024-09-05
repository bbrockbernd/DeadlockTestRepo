/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":3,"nChannels":8,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 8 different channels
- 3 different coroutines
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
package org.example.generated.test403
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ChannelManager {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>(1)
    val channel4 = Channel<Int>(1)
    val channel5 = Channel<Int>()
    val channel6 = Channel<Int>()
    val channel7 = Channel<Int>(1)
    val channel8 = Channel<Int>()
}

fun firstFunction(channelManager: ChannelManager) = runBlocking {
    launch {
        for (x in 1..5) {
            channelManager.channel1.send(x)
        }
        close(channelManager.channel1)
    }
    
    launch {
        for (x in 1..5) {
            channelManager.channel2.send(x * 2)
        }
        close(channelManager.channel2)
    }
    
    launch {
        channelManager.channel3.send(channelManager.channel1.receive())
        channelManager.channel4.send(channelManager.channel2.receive())
    }
}

fun secondFunction(channelManager: ChannelManager) = runBlocking {
    launch {
        channelManager.channel5.send(channelManager.channel3.receive())
        channelManager.channel6.send(channelManager.channel4.receive())
    }
    
    launch {
        for (x in 1..5) {
            channelManager.channel7.send(x + 10)
        }
        close(channelManager.channel7)
    }
    
    launch {
        channelManager.channel8.send(channelManager.channel7.receive())
        channelManager.channel8.send(channelManager.channel6.receive())
    }
}

fun main(): Unit{
    runBlocking {
        val channelManager = ChannelManager()
        coroutineScope {
            launch { firstFunction(channelManager) }
            launch { secondFunction(channelManager) }
        }
    }
}