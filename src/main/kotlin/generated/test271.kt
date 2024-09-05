/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":8,"nChannels":3,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 3 different channels
- 8 different coroutines
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
package org.example.generated.test271
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ChannelManager {
    val ch1 = Channel<Int>()
    val ch2 = Channel<Int>()
    val ch3 = Channel<Int>()
}

fun produceToCh1(channelManager: ChannelManager) = runBlocking {
    launch {
        channelManager.ch1.send(1)
        channelManager.ch1.send(2)
    }
}

fun produceToCh2(channelManager: ChannelManager) = runBlocking {
    launch {
        val value = channelManager.ch1.receive()
        channelManager.ch2.send(value * 2)
    }
}

fun produceToCh3(channelManager: ChannelManager) = runBlocking {
    launch {
        val value = channelManager.ch2.receive()
        channelManager.ch3.send(value + 3)
    }
}

fun consumeCh3(channelManager: ChannelManager) = runBlocking {
    launch {
        val value = channelManager.ch3.receive()
        println("Received from ch3: $value")
    }
}

fun main(): Unit = runBlocking {
    val channelManager = ChannelManager()
    
    launch { produceToCh1(channelManager) }
    launch { produceToCh2(channelManager) }
    launch { produceToCh3(channelManager) }
    launch { consumeCh3(channelManager) }
    
    launch { produceToCh1(channelManager) }
    launch { produceToCh2(channelManager) }
    launch { produceToCh3(channelManager) }
    launch { consumeCh3(channelManager) }
    
    delay(5000) // Give some time to see the deadlock
}