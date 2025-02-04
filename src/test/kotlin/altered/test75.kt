/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":8,"nChannels":1,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 1 different channels
- 8 different coroutines
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
package org.example.altered.test75
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
    suspend fun consume() {
        for (i in 1..5) {
            val value = channel.receive()
            println("Consumed: $value")
        }
    }
}

class Processor {
    suspend fun process(value: Int): Int {
        return value * 2
    }
}

class Logger {
    fun log(value: Int) {
        println("Logged: $value")
    }
}

class Aggregator {
    fun aggregate(values: List<Int>): Int {
        return values.sum()
    }
}

fun main(): Unit= runBlocking {
    val channel = Channel<Int>()

    val producer = Producer(channel)
    val consumer = Consumer(channel)
    val processor = Processor()
    val logger = Logger()
    val aggregator = Aggregator()

    launch { producer.produce() }
    launch { consumer.consume() }

    launch {
        for (i in 1..5) {
            val value = channel.receive()
            val processedValue = processor.process(value)
            logger.log(processedValue)
        }
    }

    launch {
        val values = mutableListOf<Int>()
        for (i in 1..5) {
            values.add(channel.receive())
        }
        val result = aggregator.aggregate(values)
        println("Aggregate result: $result")
    }
}

class RunChecker75: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}