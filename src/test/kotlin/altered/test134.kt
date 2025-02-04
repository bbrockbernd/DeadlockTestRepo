/* 
{"deadlock":false,"nFunctions":8,"nCoroutines":4,"nChannels":4,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
- 4 different channels
- 4 different coroutines
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
package org.example.altered.test134
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class SimpleProcessor {
    suspend fun processOne(channel: Channel<Int>) {
        for (i in 1..5) {
            channel.send(i)
        }
    }

    suspend fun processTwo(channel: Channel<Int>) {
        for (i in 6..10) {
            channel.send(i)
        }
    }
    
    suspend fun aggregateResults(channel: Channel<Int>, resultChannel: Channel<Int>) {
        var sum = 0
        for (i in 1..5) {
            sum += channel.receive()
        }
        resultChannel.send(sum)
    }
    
    suspend fun finalize(channel: Channel<Int>, finalResultChannel: Channel<String>) {
        val result = channel.receive()
        finalResultChannel.send("Final Result: $result")
    }
}

suspend fun producerOne(channel: Channel<Int>) {
    val processor = SimpleProcessor()
    processor.processOne(channel)
}

suspend fun producerTwo(channel: Channel<Int>) {
    val processor = SimpleProcessor()
    processor.processTwo(channel)
}

suspend fun consumerOne(channel: Channel<Int>, resultChannel: Channel<Int>) {
    val processor = SimpleProcessor()
    processor.aggregateResults(channel, resultChannel)
}

suspend fun finalConsumer(resultChannel: Channel<Int>, finalResultChannel: Channel<String>) {
    val processor = SimpleProcessor()
    processor.finalize(resultChannel, finalResultChannel)
}

fun main(): Unit = runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val resultChannel = Channel<Int>()
    val finalResultChannel = Channel<String>()

    launch {
        producerOne(channel1)
    }

    launch {
        producerTwo(channel2)
    }

    launch {
        consumerOne(channel1, resultChannel)
    }

    launch {
        finalConsumer(resultChannel, finalResultChannel)
    }

    println(finalResultChannel.receive())
}

class RunChecker134: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}