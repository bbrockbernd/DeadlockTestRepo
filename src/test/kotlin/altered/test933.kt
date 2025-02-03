/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":5,"nChannels":2,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 2 different channels
- 5 different coroutines
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
package org.example.altered.test933
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Processor(val inputChannel: Channel<Int>, val outputChannel: Channel<Int>) {
    suspend fun process(value: Int) {
        delay(100)
        outputChannel.send(value * 2)
    }
}

suspend fun produceNumbers(channel: Channel<Int>) {
    for (i in 1..5) {
        delay(50)
        channel.send(i)
    }
    channel.close()
}

suspend fun consumeNumbers(channel: Channel<Int>, processor: Processor) {
    for (num in channel) {
        processor.process(num)
    }
}

suspend fun printResults(channel: Channel<Int>) {
    for (result in channel) {
        println("Result: $result")
    }
}

fun runProduction(inputChannel: Channel<Int>) {
    CoroutineScope(Dispatchers.Default).launch {
        produceNumbers(inputChannel)
    }
}

fun runConsumption(inputChannel: Channel<Int>, outputChannel: Channel<Int>) {
    val processor = Processor(inputChannel, outputChannel)
    CoroutineScope(Dispatchers.Default).launch {
        consumeNumbers(inputChannel, processor)
    }
}

fun main(): Unit= runBlocking {
    val inputChannel = Channel<Int>()
    val outputChannel = Channel<Int>()

    launch { runProduction(inputChannel) }
    launch { runConsumption(inputChannel, outputChannel) }
    launch { printResults(outputChannel) }

    delay(2000) // Give enough time for processing before ending main
}

class RunChecker933: RunCheckerBase() {
    override fun block() = main()
}