/* 
{"deadlock":false,"nFunctions":7,"nCoroutines":3,"nChannels":4,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 4 different channels
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
package org.example.altered.test155
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Processor {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()
    val channelC = Channel<Int>()
    val channelD = Channel<Int>()
}

fun sendToChannelA(channel: Channel<Int>, value: Int) {
    runBlocking {
        channel.send(value)
    }
}

fun receiveFromChannelA(channel: Channel<Int>): Int {
    var result = 0
    runBlocking {
        result = channel.receive()
    }
    return result
}

fun processChannelsAtoB(processor: Processor) {
    runBlocking {
        val value = receiveFromChannelA(processor.channelA)
        processor.channelB.send(value + 1)
    }
}

fun processChannelsBtoC(processor: Processor) {
    runBlocking {
        val value = processor.channelB.receive()
        processor.channelC.send(value + 1)
    }
}

fun processChannelsCtoD(processor: Processor) {
    runBlocking {
        val value = processor.channelC.receive()
        processor.channelD.send(value + 1)
    }
}

fun initCoroutines(processor: Processor) {
    runBlocking {
        launch {
            processChannelsAtoB(processor)
        }

        launch {
            processChannelsBtoC(processor)
        }

        launch {
            processChannelsCtoD(processor)
        }
    }
}

fun main(): Unit{
    val processor = Processor()
    
    sendToChannelA(processor.channelA, 0)
    initCoroutines(processor)
    
    runBlocking {
        val result = processor.channelD.receive()
        println("Final result: $result") // Output should be 3
    }
}

class RunChecker155: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}