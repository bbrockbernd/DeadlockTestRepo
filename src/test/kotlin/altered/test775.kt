/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":5,"nChannels":3,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
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
package org.example.altered.test775
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(private val channel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            channel.send(i)
        }
    }
}

class Consumer(private val channel: Channel<Int>) {
    suspend fun consume(): Int {
        var total = 0
        repeat(5) {
            total += channel.receive()
        }
        return total
    }
}

class Processor(private val input: Channel<Int>, private val output: Channel<Int>) {
    suspend fun process() {
        while (true) {
            val item = input.receive()
            output.send(item * 2)
        }
    }
}

fun startProducer(channel: Channel<Int>) = CoroutineScope(Dispatchers.Default).launch {
    val producer = Producer(channel)
    producer.produce()
}

fun startConsumer(channel: Channel<Int>) = CoroutineScope(Dispatchers.Default).launch {
    val consumer = Consumer(channel)
    consumer.consume()
}

fun startProcessor(input: Channel<Int>, output: Channel<Int>) = CoroutineScope(Dispatchers.Default).launch {
    val processor = Processor(input, output)
    processor.process()
}

fun main(): Unit= runBlocking {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()
    val channelC = Channel<Int>()

    startProducer(channelA)
    startProcessor(channelA, channelB)
    startProcessor(channelB, channelC)
    startConsumer(channelC)

    delay(1000)
}

class RunChecker775: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}