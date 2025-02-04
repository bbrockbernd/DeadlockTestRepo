/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":1,"nChannels":3,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 3 different channels
- 1 different coroutines
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
package org.example.altered.test701
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer {
    private val channel: Channel<Int> = Channel()

    fun getChannel(): Channel<Int> = channel

    suspend fun produce() {
        for (i in 1..5) {
            channel.send(i)
        }
        channel.close()
    }
}

class Consumer(private val channel: Channel<Int>) {
    suspend fun consume(): Int {
        var result = 0
        for (element in channel) {
            result += element
        }
        return result
    }
}

class Processor(private val channel: Channel<Int>) {
    private val processedChannel: Channel<Int> = Channel()

    fun getProcessedChannel(): Channel<Int> = processedChannel

    suspend fun process() {
        for (element in channel) {
            processedChannel.send(element * 2)
        }
        processedChannel.close()
    }
}

fun createProducer(): Producer {
    return Producer()
}

fun createConsumer(channel: Channel<Int>): Consumer {
    return Consumer(channel)
}

fun createProcessor(channel: Channel<Int>): Processor {
    return Processor(channel)
}

fun main(): Unit= runBlocking {
    val producer = createProducer()
    val processor = createProcessor(producer.getChannel())
    val consumer = createConsumer(processor.getProcessedChannel())

    launch {
        producer.produce()
    }

    launch {
        processor.process()
    }

    launch {
        val result = consumer.consume()
        println("Result: $result")
    }
}

class RunChecker701: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}