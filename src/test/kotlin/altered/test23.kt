/* 
{"deadlock":false,"nFunctions":7,"nCoroutines":8,"nChannels":4,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 4 different channels
- 8 different coroutines
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
package org.example.altered.test23
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class Processor(private val input1: Channel<Int>, private val input2: Channel<Int>, private val output: Channel<Int>) {
    suspend fun processInput() {
        val value1 = input1.receive()
        val value2 = input2.receive()
        output.send(value1 + value2)
    }
}

class Splitter(private val input: Channel<Int>, private val output1: Channel<Int>, private val output2: Channel<Int>) {
    suspend fun splitInput() {
        val value = input.receive()
        output1.send(value / 2)
        output2.send(value / 2)
    }
}

class Aggregator(private val input: Channel<Int>) {
    suspend fun aggregate(): Int {
        var sum = 0
        repeat(2) {
            sum += input.receive()
        }
        return sum
    }
}

fun producer(channel: Channel<Int>, value: Int) = GlobalScope.launch {
    channel.send(value)
}

fun initiateProcessing(input1: Channel<Int>, input2: Channel<Int>, output: Channel<Int>) = GlobalScope.launch {
    val processor = Processor(input1, input2, output)
    processor.processInput()
}

fun initiateSplitting(input: Channel<Int>, output1: Channel<Int>, output2: Channel<Int>) = GlobalScope.launch {
    val splitter = Splitter(input, output1, output2)
    splitter.splitInput()
}

fun initiateAggregation(input: Channel<Int>, result: CompletableDeferred<Int>) = GlobalScope.launch {
    val aggregator = Aggregator(input)
    result.complete(aggregator.aggregate())
}

fun main(): Unit= runBlocking {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()
    val channelC = Channel<Int>()
    val channelD = Channel<Int>()

    val result = CompletableDeferred<Int>()

    producer(channelA, 10)
    producer(channelB, 20)
    initiateProcessing(channelA, channelB, channelC)
    initiateSplitting(channelC, channelD, channelD)
    initiateAggregation(channelD, result)

    println("Aggregated Result: ${result.await()}")
}

class RunChecker23: RunCheckerBase() {
    override fun block() = main()
}