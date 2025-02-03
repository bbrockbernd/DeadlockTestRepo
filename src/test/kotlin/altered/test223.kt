/* 
{"deadlock":true,"nFunctions":6,"nCoroutines":1,"nChannels":1,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 1 different channels
- 1 different coroutines
- 4 different classes

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
package org.example.altered.test223
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(val channel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            channel.send(i)
        }
    }
}

class Consumer(val channel: Channel<Int>) {
    suspend fun consume() {
        for (i in 1..5) {
            channel.receive()
        }
    }
}

class Processor(val channel: Channel<Int>) {
    suspend fun process() {
        for (i in 1..5) {
            val value = channel.receive()
            channel.send(value * 2)
        }
    }
}

class Printer(val channel: Channel<Int>) {
    suspend fun print() {
        for (i in 1..5) {
            println(channel.receive())
        }
    }
}

fun startProducer(producer: Producer) = runBlocking {
    launch {
        producer.produce()
    }
}

fun startConsumer(consumer: Consumer) = runBlocking {
    launch {
        consumer.consume()
    }
}

fun main(): Unit = runBlocking {
    val channel = Channel<Int>()
    val producer = Producer(channel)
    val consumer = Consumer(channel)
    val processor = Processor(channel)
    val printer = Printer(channel)

    launch {
        startProducer(producer)
    }
    launch {
        startConsumer(consumer)
    }
    launch {
        processor.process()
    }
    launch {
        printer.print()
    }
}

class RunChecker223: RunCheckerBase() {
    override fun block() = main()
}