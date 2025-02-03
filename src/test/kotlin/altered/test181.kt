/* 
{"deadlock":false,"nFunctions":7,"nCoroutines":6,"nChannels":8,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 8 different channels
- 6 different coroutines
- 2 different classes

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
package org.example.altered.test181
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class DataChannel(val channel: Channel<String>)

class ProcessChannel(val channel: Channel<Int>)

fun createChannels(): List<Channel<Any>> {
    return listOf(
        Channel<Any>(),
        Channel<Any>(),
        Channel<Any>(),
        Channel<Any>(),
        Channel<Any>(),
        Channel<Any>(),
        Channel<Any>(),
        Channel<Any>()
    )
}

fun dataProducer(channel: Channel<String>) {
    runBlocking {
        launch {
            channel.send("Data 1")
            channel.send("Data 2")
            channel.send("Data 3")
        }
    }
}

fun dataConsumer(channel: Channel<String>) {
    runBlocking {
        launch {
            repeat(3) {
                println(channel.receive())
            }
        }
    }
}

fun processProducer(channel: Channel<Int>) {
    runBlocking {
        launch {
            channel.send(1)
            channel.send(2)
            channel.send(3)
        }
    }
}

fun processConsumer(channel: Channel<Int>) {
    runBlocking {
        launch {
            repeat(3) {
                println(channel.receive())
            }
        }
    }
}

fun combineChannels(
    dataChannel: Channel<String>,
    processChannel: Channel<Int>,
    combinedChannel: Channel<Any>
) {
    runBlocking {
        launch {
            repeat(3) {
                combinedChannel.send(dataChannel.receive())
                combinedChannel.send(processChannel.receive())
            }
        }
    }
}

fun consumeCombined(combinedChannel: Channel<Any>) {
    runBlocking {
        launch {
            repeat(6) {
                println(combinedChannel.receive())
            }
        }
    }
}

fun main(): Unit {
    val stringChannel1 = Channel<String>()
    val stringChannel2 = Channel<String>()
    val intChannel1 = Channel<Int>()
    val intChannel2 = Channel<Int>()
    val combinedChannel1 = Channel<Any>()
    val combinedChannel2 = Channel<Any>()
    val dataChannel = DataChannel(stringChannel1)
    val processChannel = ProcessChannel(intChannel1)

    dataProducer(dataChannel.channel)
    dataConsumer(dataChannel.channel)
    processProducer(processChannel.channel)
    processConsumer(processChannel.channel)

    combineChannels(stringChannel1, intChannel1, combinedChannel1)
    consumeCombined(combinedChannel1)

    combineChannels(stringChannel2, intChannel2, combinedChannel2)
    consumeCombined(combinedChannel2)
}

class RunChecker181: RunCheckerBase() {
    override fun block() = main()
}