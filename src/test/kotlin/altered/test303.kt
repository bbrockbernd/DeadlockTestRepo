/* 
{"deadlock":false,"nFunctions":6,"nCoroutines":2,"nChannels":8,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 8 different channels
- 2 different coroutines
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
package org.example.altered.test303
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ChannelManager {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>(5)
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>(5)

    fun sendToChannel1(value: Int) = runBlocking {
        channel1.send(value)
    }

    fun receiveFromChannel1(): Int = runBlocking {
        channel1.receive()
    }
}

class Processor1 {
    val channel5 = Channel<Int>()
    val channel6 = Channel<Int>(5)

    fun processAndSend(channel: Channel<Int>, value: Int) = runBlocking {
        val processedValue = value * 2
        channel.send(processedValue)
    }

    fun receiveProcessAndSend(source: Channel<Int>, destination: Channel<Int>) = runBlocking {
        val receivedValue = source.receive()
        destination.send(receivedValue + 3)
    }
}

class Processor2 {
    val channel7 = Channel<Int>()
    val channel8 = Channel<Int>(5)

    fun receiveAndProcess(channel: Channel<Int>): Int = runBlocking {
        val value = channel.receive()
        value + 5
    }
}

fun runFirstCoroutine(manager: ChannelManager, processor1: Processor1) = runBlocking {
    launch {
        for (i in 1..5) {
            processor1.processAndSend(manager.channel2, i)
            processor1.receiveProcessAndSend(manager.channel2, manager.channel4)
        }
    }
}

fun runSecondCoroutine(manager: ChannelManager, processor2: Processor2) = runBlocking {
    launch {
        for (i in 1..5) {
            manager.sendToChannel1(i)
            processor2.receiveAndProcess(manager.channel1)
        }
    }
}

fun main(): Unit= runBlocking {
    val manager = ChannelManager()
    val processor1 = Processor1()
    val processor2 = Processor2()

    runFirstCoroutine(manager, processor1)
    runSecondCoroutine(manager, processor2)
}

class RunChecker303: RunCheckerBase() {
    override fun block() = main()
}