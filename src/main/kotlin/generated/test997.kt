/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":4,"nChannels":5,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 5 different channels
- 4 different coroutines
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
package org.example.generated.test997
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(private val outChannel: Channel<Int>) {
    suspend fun produce() {
        repeat(5) {
            outChannel.send(it)
        }
        outChannel.close()
    }
}

class Processor(
    private val inChannel: Channel<Int>,
    private val outChannel: Channel<Int>
) {
    suspend fun process() {
        for (x in inChannel) {
            val result = x * 2
            outChannel.send(result)
        }
        outChannel.close()
    }
}

class Consumer(private val inChannel: Channel<Int>) {
    suspend fun consume(name: String) {
        for (x in inChannel) {
            println("Consumer $name received: $x")
        }
    }
}

fun startProducer(channel: Channel<Int>) {
    val producer = Producer(channel)
    GlobalScope.launch {
        producer.produce()
    }
}

fun startProcessor(inChannel: Channel<Int>, outChannel: Channel<Int>) {
    val processor = Processor(inChannel, outChannel)
    GlobalScope.launch {
        processor.process()
    }
}

fun startConsumer(channel: Channel<Int>, name: String) {
    val consumer = Consumer(channel)
    GlobalScope.launch {
        consumer.consume(name)
    }
}

fun main(): Unit= runBlocking<Unit> {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()

    startProducer(channel1)
    startProcessor(channel1, channel2)
    startProcessor(channel2, channel3)
    startConsumer(channel3, "A")
    startConsumer(channel3, "B")
}