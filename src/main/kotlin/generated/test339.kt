/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":8,"nChannels":5,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 5 different channels
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
package org.example.generated.test339
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class ChannelManager {
    val channel1 = Channel<Int>() //unbuffered
    val channel2 = Channel<Int>(5) // buffered with capacity 5
    val channel3 = Channel<Int>() // unbuffered
    val channel4 = Channel<Int>(5) // buffered with capacity 5
    val channel5 = Channel<Int>() // unbuffered
}

fun producer(channelManager: ChannelManager) = runBlocking {
    launch {
        channelManager.channel1.send(1)
        channelManager.channel2.send(2)
    }

    launch {
        channelManager.channel3.send(3)
        channelManager.channel4.send(4)
    }

    launch {
        channelManager.channel5.send(5)
    }
}

fun consumer(channelManager: ChannelManager) = runBlocking {
    launch {
        println(channelManager.channel1.receive())
        println(channelManager.channel3.receive())
    }

    launch {
        println(channelManager.channel2.receive())
        println(channelManager.channel4.receive())
    }

    launch {
        println(channelManager.channel5.receive())
    }
}

fun main(): Unit = runBlocking {
    val channelManager = ChannelManager()

    launch {
        producer(channelManager)
    }

    launch {
        consumer(channelManager)
    }

    delay(1000) // to keep the main coroutine alive to let others finish
}