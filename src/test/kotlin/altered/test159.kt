/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":8,"nChannels":4,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 4 different channels
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
package org.example.altered.test159
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

data class Data(val value: Int)

class Processor {
    val processedChannel = Channel<Data>()

    suspend fun process(inputChannel: Channel<Data>, outputChannel: Channel<Data>) {
        for (data in inputChannel) {
            outputChannel.send(Data(data.value * 2))
        }
    }
}

suspend fun generator(channel: Channel<Data>) {
    repeat(10) {
        channel.send(Data(it))
    }
    channel.close()
}

suspend fun consumer(channel: Channel<Data>) {
    for (data in channel) {
        println("Consumed: ${data.value}")
    }
}

fun main(): Unit= runBlocking {
    val inputChannel = Channel<Data>()
    val intermediateChannel1 = Channel<Data>()
    val intermediateChannel2 = Channel<Data>()
    val outputChannel = Channel<Data>()

    val processor1 = Processor()
    val processor2 = Processor()

    launch { generator(inputChannel) }
    launch { processor1.process(inputChannel, intermediateChannel1) }
    launch { processor2.process(intermediateChannel1, intermediateChannel2) }
    launch {
        for (data in intermediateChannel2) {
            processor2.processedChannel.send(data)
        }
        intermediateChannel2.close()
    }
    launch { consumer(processor2.processedChannel) }

    launch {
        for (data in processor2.processedChannel) {
            outputChannel.send(data)
        }
        outputChannel.close()
    }
    launch { consumer(outputChannel) }
}

class RunChecker159: RunCheckerBase() {
    override fun block() = main()
}