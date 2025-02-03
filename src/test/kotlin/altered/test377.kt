/* 
{"deadlock":false,"nFunctions":6,"nCoroutines":5,"nChannels":5,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 5 different channels
- 5 different coroutines
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
package org.example.altered.test377
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(val channel: Channel<Int>) {
    suspend fun produceValues(values: List<Int>) {
        for (value in values) {
            channel.send(value)
        }
        channel.close()
    }
}

class Consumer(val channel: Channel<Int>, val results: MutableList<Int>) {
    suspend fun consumeValues() {
        for (value in channel) {
            results.add(value)
        }
    }
}

suspend fun processResults(results: List<Int>): Int {
    return results.sum()
}

suspend fun getData(channel: Channel<Int>, values: List<Int>) {
    for (value in values) {
        channel.send(value)
    }
    channel.close()
}

fun mainProcessing() = runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()

    val results1 = mutableListOf<Int>()
    val results2 = mutableListOf<Int>()

    val producer1 = Producer(channel1)
    val producer2 = Producer(channel2)
    val consumer1 = Consumer(channel3, results1)
    val consumer2 = Consumer(channel4, results2)

    launch {
        producer1.produceValues(listOf(1, 2, 3, 4))
    }

    launch {
        producer2.produceValues(listOf(5, 6, 7, 8))
    }

    launch {
        producer1.channel.consumeEach { value ->
            channel3.send(value)
        }
        channel3.close()
    }

    launch {
        producer2.channel.consumeEach { value ->
            channel4.send(value)
        }
        channel4.close()
    }

    launch {
        consumer1.consumeValues()
    }

    launch {
        consumer2.consumeValues()
    }

    getData(channel5, listOf(9, 10, 11, 12))

    val finalResults1 = processResults(results1)
    val finalResults2 = processResults(results2)

    println("Results from Consumer1: $finalResults1")
    println("Results from Consumer2: $finalResults2")
}

fun main(): Unit{
    mainProcessing()
}

class RunChecker377: RunCheckerBase() {
    override fun block() = main()
}