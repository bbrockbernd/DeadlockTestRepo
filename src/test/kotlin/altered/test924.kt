/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":3,"nChannels":5,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 5 different channels
- 3 different coroutines
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
package org.example.altered.test924
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Processor(val inputChannel: Channel<Int>, val outputChannel: Channel<Int>) {
    suspend fun process() {
        for (item in inputChannel) {
            outputChannel.send(item * 2)
        }
    }
}

class Aggregator(val inputChannel1: Channel<Int>, val inputChannel2: Channel<Int>, val outputChannel: Channel<String>) {
    suspend fun aggregate() {
        for (item1 in inputChannel1) {
            for (item2 in inputChannel2) {
                outputChannel.send("Aggregated Result: ${item1 + item2}")
            }
        }
    }
}

class Printer(val inputChannel: Channel<String>) {
    suspend fun printAll() {
        for (item in inputChannel) {
            println(item)
        }
    }
}

suspend fun generateNumbers(channel: Channel<Int>) {
    for (i in 1..5) {
        channel.send(i)
    }
    channel.close()
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val processedChannel1 = Channel<Int>()
    val processedChannel2 = Channel<Int>()
    val aggregatedChannel = Channel<String>()

    val processor1 = Processor(channel1, processedChannel1)
    val processor2 = Processor(channel2, processedChannel2)
    val aggregator = Aggregator(processedChannel1, processedChannel2, aggregatedChannel)
    val printer = Printer(aggregatedChannel)

    launch {
        generateNumbers(channel1)
    }

    launch {
        generateNumbers(channel2)
    }

    launch {
        processor1.process()
    }

    launch {
        processor2.process()
    }

    launch {
        aggregator.aggregate()
    }

    launch {
        printer.printAll()
    }
}

class RunChecker924: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}