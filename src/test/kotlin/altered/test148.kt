/* 
{"deadlock":false,"nFunctions":8,"nCoroutines":1,"nChannels":4,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
- 4 different channels
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
package org.example.altered.test148
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer1(private val outChannel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            outChannel.send(i)
        }
        outChannel.close()
    }
}

class Consumer1(private val inChannel: Channel<Int>) {
    suspend fun consume() {
        for (i in inChannel) {
            println("Consumer1 received: $i")
        }
    }
}

suspend fun processChannel1(input: Channel<Int>, output: Channel<Int>) {
    for (i in input) {
        output.send(i * 2)
    }
    output.close()
}

suspend fun processChannel2(input: Channel<Int>, output: Channel<Int>) {
    for (i in input) {
        output.send(i + 1)
    }
    output.close()
}

fun CoroutineScope.processChannel3(input: Channel<Int>, output: Channel<Int>) = launch {
    for (i in input) {
        output.send(i - 1)
    }
    output.close()
}

fun CoroutineScope.startProducer1(channel: Channel<Int>) = launch {
    val producer1 = Producer1(channel)
    producer1.produce()
}

fun CoroutineScope.startConsumer1(channel: Channel<Int>) = launch {
    val consumer1 = Consumer1(channel)
    consumer1.consume()
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()

    startProducer1(channel1)
    processChannel1(channel1, channel2)
    processChannel2(channel2, channel3)
    processChannel3(channel3, channel4)
    startConsumer1(channel4)
}

class RunChecker148: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}