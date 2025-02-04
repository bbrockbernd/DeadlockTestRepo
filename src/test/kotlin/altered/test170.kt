/* 
{"deadlock":false,"nFunctions":6,"nCoroutines":6,"nChannels":3,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 3 different channels
- 6 different coroutines
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
package org.example.altered.test170
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class DataProcessor {
    suspend fun processData(channel: Channel<Int>): Int {
        var sum = 0
        for (i in 1..5) {
            val value = channel.receive()
            sum += value
        }
        return sum
    }
}

suspend fun sendData(channel: Channel<Int>) {
    for (i in 1..5) {
        channel.send(i)
    }
}

fun setupPipeline(
    inputChannel: Channel<Int>,
    outputChannel: Channel<Int>,
    errorChannel: Channel<String>
) {
    GlobalScope.launch {
        try {
            val sum = DataProcessor().processData(inputChannel)
            outputChannel.send(sum)
        } catch (e: Exception) {
            errorChannel.send("Error: ${e.message}")
        }
    }
}

suspend fun receiveResults(outputChannel: Channel<Int>, errorChannel: Channel<String>) {
    try {
        val result = outputChannel.receive()
        println("Result: $result")
    } catch (e: Exception) {
        println(errorChannel.receive())
    }
}

fun startCoroutine(
    inputChannel: Channel<Int>,
    outputChannel: Channel<Int>,
    errorChannel: Channel<String>
) {
    GlobalScope.launch {
        sendData(inputChannel)
    }
    setupPipeline(inputChannel, outputChannel, errorChannel)
}

fun main(): Unit= runBlocking {
    val inputChannel = Channel<Int>()
    val outputChannel = Channel<Int>()
    val errorChannel = Channel<String>()

    startCoroutine(inputChannel, outputChannel, errorChannel)
    receiveResults(outputChannel, errorChannel)
}

class RunChecker170: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}