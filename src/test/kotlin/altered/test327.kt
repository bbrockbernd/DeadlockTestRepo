/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":1,"nChannels":4,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 4 different channels
- 1 different coroutines
- 5 different classes

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
package org.example.altered.test327
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer1(val channel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            channel.send(i)
        }
        channel.close()
    }
}

class Producer2(val channel: Channel<Int>) {
    suspend fun produce() {
        for (i in 6..10) {
            channel.send(i)
        }
        channel.close()
    }
}

class Consumer1(val channel: Channel<Int>) {
    suspend fun consume() {
        for (i in channel) {
            println("Consumer1 received: $i")
        }
    }
}

class Consumer2(val channel: Channel<Int>) {
    suspend fun consume() {
        for (i in channel) {
            println("Consumer2 received: $i")
        }
    }
}

class Processor {
    suspend fun process(channel1: Channel<Int>, channel2: Channel<Int>, outChannel: Channel<Int>) {
        for (i in channel1) {
            outChannel.send(i * 2)
        }
        for (i in channel2) {
            outChannel.send(i * 2)
        }
        outChannel.close()
    }
}

fun main(): Unit = runBlocking {
    val channel1 = Channel<Int>(3)
    val channel2 = Channel<Int>(3)
    val outChannel1 = Channel<Int>(3)
    val outChannel2 = Channel<Int>(3)

    val producer1 = Producer1(channel1)
    val producer2 = Producer2(channel2)
    val consumer1 = Consumer1(outChannel1)
    val consumer2 = Consumer2(outChannel2)
    val processor = Processor()

    launch {
        producer1.produce()
    }
    launch {
        producer2.produce()
    }
    launch {
        processor.process(channel1, channel2, outChannel1)
    }
    launch {
        processor.process(channel1, channel2, outChannel2)
    }
    launch {
        consumer1.consume()
    }
    launch {
        consumer2.consume()
    }
}

class RunChecker327: RunCheckerBase() {
    override fun block() = main()
}