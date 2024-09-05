/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":2,"nChannels":5,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 5 different channels
- 2 different coroutines
- 0 different classes

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
package org.example.generated.test615
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()

    launch { producer(channel1, 1) }
    launch { consumer(channel1, channel2) }

    launch { producer(channel3, 2) }
    launch { consumer(channel3, channel4) }

    launch { finalConsumer(channel2, channel4, channel5) }

    launch { endTask(channel5) }
}

suspend fun producer(channel: Channel<Int>, value: Int) {
    channel.send(value)
    channel.close()
}

suspend fun consumer(inputChannel: Channel<Int>, outputChannel: Channel<Int>) {
    for (value in inputChannel) {
        outputChannel.send(value * 2)
    }
    outputChannel.close()
}

suspend fun finalConsumer(channel1: Channel<Int>, channel2: Channel<Int>, outputChannel: Channel<Int>) {
    val value1 = channel1.receive()
    val value2 = channel2.receive()
    outputChannel.send(value1 + value2)
    outputChannel.close()
}

suspend fun endTask(channel: Channel<Int>) {
    val result = channel.receive()
    println("Final result: $result")
}