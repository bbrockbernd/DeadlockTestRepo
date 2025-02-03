/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":7,"nChannels":7,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 7 different channels
- 7 different coroutines
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
package org.example.altered.test98
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class DataProducer1 {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()

    suspend fun produce() {
        for (i in 1..5) {
            channel1.send(i)
            channel2.send(i * 2)
        }
        channel1.close()
        channel2.close()
    }
}

class DataProducer2 {
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()

    suspend fun produce() {
        for (i in 6..10) {
            channel3.send(i)
            channel4.send(i * 2)
        }
        channel3.close()
        channel4.close()
    }
}

suspend fun processAndSendData(channelInput: Channel<Int>, channelOutput: Channel<Int>) {
    for (value in channelInput) {
        channelOutput.send(value + 1)
    }
    channelOutput.close()
}

suspend fun aggregateResults(vararg channels: Channel<Int>): Int {
    var result = 0
    coroutineScope {
        channels.forEach { channel ->
            launch {
                for (value in channel) {
                    result += value
                }
            }
        }
    }
    return result
}

fun main(): Unit= runBlocking {
    val producer1 = DataProducer1()
    val producer2 = DataProducer2()
    val channel5 = Channel<Int>()
    val channel6 = Channel<Int>()
    val channel7 = Channel<Int>()

    // Launch producers
    launch { producer1.produce() }
    launch { producer2.produce() }

    // Process data and send to new channels
    launch { processAndSendData(producer1.channel1, channel5) }
    launch { processAndSendData(producer1.channel2, channel6) }
    launch { processAndSendData(producer2.channel3, channel6) }
    launch { processAndSendData(producer2.channel4, channel7) }

    // Aggregate results
    val result = aggregateResults(channel5, channel6, channel7)

    println("Aggregated result: $result")
}

class RunChecker98: RunCheckerBase() {
    override fun block() = main()
}