/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":5,"nChannels":3,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 3 different channels
- 5 different coroutines
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
package org.example.generated.test657
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(private val channel: Channel<Int>) {
    suspend fun produce() {
        repeat(5) {
            channel.send(it)
        }
        channel.close()
    }
}

class Consumer(private val channel: Channel<Int>, private val resultChannel: Channel<Int>) {
    suspend fun consume() {
        for (item in channel) {
            resultChannel.send(item * 2)
        }
        resultChannel.close()
    }
}

class ResultProcessor(private val resultChannel: Channel<Int>) {
    suspend fun process() {
        for (result in resultChannel) {
            println("Processed result: $result")
        }
    }
}

suspend fun generateData(channel1: Channel<Int>, channel2: Channel<Int>, channel3: Channel<Int>) {
    val p1 = Producer(channel1)
    val p2 = Producer(channel2)
    
    coroutineScope {
        launch { p1.produce() }
        launch { p2.produce() }
    }
    
    val consumer = Consumer(channel1, channel3)
    launch { consumer.consume() }
}

suspend fun processData(channel3: Channel<Int>) {
    val resultProcessor = ResultProcessor(channel3)
    coroutineScope {
        launch { resultProcessor.process() }
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    
    generateData(channel1, channel2, channel3)
    processData(channel3)
}