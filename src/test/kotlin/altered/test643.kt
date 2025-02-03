/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":3,"nChannels":2,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 2 different channels
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
package org.example.altered.test643
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class DataProcessor {
    val inputChannel = Channel<Int>()
    val outputChannel = Channel<Int>()

    fun startProcessing() = runBlocking {
        launch { processReceivedData() }
        launch { sendProcessedData() }
    }

    private suspend fun processReceivedData() {
        coroutineScope {
            for (i in 1..5) {
                val received = inputChannel.receive()
                println("Processing data: $received")
                delay(100)
                outputChannel.send(received * 2)
            }
        }
    }

    private suspend fun sendProcessedData() {
        coroutineScope {
            for (i in 1..5) {
                val processed = outputChannel.receive()
                println("Sending processed data: $processed")
            }
        }
    }
}

suspend fun producer(channel: Channel<Int>) {
    for (i in 1..5) {
        println("Sending data: $i")
        channel.send(i)
        delay(100)
    }
}

fun main(): Unit= runBlocking {
    val dataProcessor = DataProcessor()

    launch { producer(dataProcessor.inputChannel) }
    dataProcessor.startProcessing()
}

class RunChecker643: RunCheckerBase() {
    override fun block() = main()
}