/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":1,"nChannels":7,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 7 different channels
- 1 different coroutines
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
package org.example.altered.test209
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ProcessorA(val input: Channel<Int>, val output: Channel<Int>)
class ProcessorB(val input: Channel<Int>, val output: Channel<Int>)

val channel1 = Channel<Int>()
val channel2 = Channel<Int>()
val channel3 = Channel<Int>()
val channel4 = Channel<Int>()
val channel5 = Channel<Int>()
val channel6 = Channel<Int>()
val channel7 = Channel<Int>()

fun processA1(processor: ProcessorA) {
    runBlocking {
        while (true) {
            val value = processor.input.receive()
            processor.output.send(value)
        }
    }
}

fun processA2(processor: ProcessorA) {
    runBlocking {
        while (true) {
            val value = processor.input.receive()
            processor.output.send(value * 2)
        }
    }
}

fun processB1(processor: ProcessorB) {
    runBlocking {
        while (true) {
            val value = processor.input.receive()
            processor.output.send(value / 2)
        }
    }
}

fun initiateProcessing() {
    runBlocking {
        launch {
            val processorA1 = ProcessorA(channel1, channel2)
            val processorA2 = ProcessorA(channel3, channel4)
            val processorB1 = ProcessorB(channel5, channel6)

            launch { processA1(processorA1) }
            launch { processA2(processorA2) }
            launch { processB1(processorB1) }

            channel1.send(10)
            channel4.send(20)
            channel6.send(30)

            channel7.send(channel2.receive())
            channel7.send(channel3.receive())
            channel7.send(channel5.receive())
        }

        launch {
            val resultChannel = Channel<Int>()
            launch {
                val valueFromCh7 = channel7.receive()
                resultChannel.send(valueFromCh7)
            }
            val finalResult = resultChannel.receive()
            println("Final result: $finalResult")
        }
    }
}

fun main(): Unit{
    initiateProcessing()
}

class RunChecker209: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}