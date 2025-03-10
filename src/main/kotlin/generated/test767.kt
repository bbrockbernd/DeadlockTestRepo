/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":3,"nChannels":5,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 5 different channels
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
package org.example.generated.test767
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ChannelManager {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()
}

fun functionA(channelManager: ChannelManager) = runBlocking {
    launch {
        val value = channelManager.channel1.receive()
        channelManager.channel2.send(value)
    }
}

fun functionB(channelManager: ChannelManager) = runBlocking {
    launch {
        val value = channelManager.channel2.receive()
        channelManager.channel3.send(value)
    }
}

fun functionC(channelManager: ChannelManager) = runBlocking {
    launch {
        val value = channelManager.channel3.receive()
        channelManager.channel1.send(value)
    }
}

fun main(): Unit= runBlocking {
    val channelManager = ChannelManager()
    
    launch { functionA(channelManager) }
    launch { functionB(channelManager) }
    launch { functionC(channelManager) }

    channelManager.channel1.send(1) // Initial send to kick things off

    delay(1000) // Enough time to potentially detect deadlock
}