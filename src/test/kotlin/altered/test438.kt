/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":6,"nChannels":3,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 3 different channels
- 6 different coroutines
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
package org.example.altered.test438
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ChannelManager {
    val channel1 = Channel<Int>(5)
    val channel2 = Channel<String>(5)
    val channel3 = Channel<Double>(5)
}

fun sendIntegers(channel: Channel<Int>, limit: Int) = runBlocking {
    for (i in 1..limit) {
        channel.send(i)
    }
    channel.close()
}

fun sendStrings(channel: Channel<String>, words: List<String>) = runBlocking {
    for (word in words) {
        channel.send(word)
    }
    channel.close()
}

fun receiveIntegers(channel: Channel<Int>) = runBlocking {
    for (element in channel) {
        println("Received Integer: $element")
    }
}

fun receiveStrings(channel: Channel<String>) = runBlocking {
    for (element in channel) {
        println("Received String: $element")
    }
}

fun main(): Unit= runBlocking {
    val manager = ChannelManager()

    launch {
        sendIntegers(manager.channel1, 5)
    }

    launch {
        sendStrings(manager.channel2, listOf("Kotlin", "Coroutines", "Channels"))
    }

    launch {
        for (i in 1..5) {
            manager.channel3.send(i.toDouble())
        }
        manager.channel3.close()
    }

    launch {
        receiveIntegers(manager.channel1)
    }

    launch {
        receiveStrings(manager.channel2)
    }

    launch {
        for (element in manager.channel3) {
            println("Received Double: $element")
        }
    }
}

class RunChecker438: RunCheckerBase() {
    override fun block() = main()
}