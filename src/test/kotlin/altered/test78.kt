/* 
{"deadlock":true,"nFunctions":2,"nCoroutines":6,"nChannels":3,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 3 different channels
- 6 different coroutines
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
package org.example.altered.test78
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class Producer(private val outChannel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            outChannel.send(i)
        }
    }
}

class Consumer(private val inChannel: Channel<Int>, private val outChannel: Channel<Int>) {
    suspend fun consumeAndForward() {
        for (i in 1..5) {
            val received = inChannel.receive()
            outChannel.send(received)
        }
    }
}

class Processor(private val inChannel: Channel<Int>, private val outChannel: Channel<Int>) {
    suspend fun process() {
        for (i in 1..5) {
            val toProcess = inChannel.receive()
            outChannel.send(toProcess * 2)
        }
    }
}

suspend fun produceData(channel: Channel<Int>) {
    val producer = Producer(channel)
    producer.produce()
}

suspend fun consumeAndProcess(inChannel: Channel<Int>, outChannel: Channel<Int>) {
    val consumer = Consumer(inChannel, outChannel)
    consumer.consumeAndForward()
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()

    launch { produceData(channel1) }
    launch { consumeAndProcess(channel1, channel2) }
    launch { consumeAndProcess(channel2, channel3) }
    launch { consumeAndProcess(channel3, channel1) }
    launch { produceData(channel2) }
    launch { 
        val processor = Processor(channel3, channel1)
        processor.process()
    }

    delay(5000)
}

class RunChecker78: RunCheckerBase() {
    override fun block() = main()
}