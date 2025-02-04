/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":4,"nChannels":3,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 3 different channels
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
package org.example.altered.test714
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class Processor {
    suspend fun processData(channel: Channel<Int>): Int {
        val data = channel.receive()
        return data * 2
    }
}

class Logger {
    suspend fun logData(channel: Channel<String>, message: String) {
        channel.send("Log: $message")
    }
}

class Aggregator {
    suspend fun aggregateData(channel1: Channel<Int>, channel2: Channel<Int>): Int {
        val data1 = channel1.receive()
        val data2 = channel2.receive()
        return data1 + data2
    }
}

fun sendData(channel: Channel<Int>, value: Int) {
    GlobalScope.launch {
        channel.send(value)
    }
}

suspend fun monitorChannel(channel: Channel<String>) {
    val log = channel.receive()
    println(log)
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val logChannel = Channel<String>()

    val processor = Processor()
    val logger = Logger()
    val aggregator = Aggregator()

    launch {
        sendData(channel1, 5)
    }

    launch {
        sendData(channel2, 10)
    }

    launch {
        val result = aggregator.aggregateData(channel1, channel2)
        logger.logData(logChannel, "Aggregated result: $result")
    }

    launch {
        monitorChannel(logChannel)
    }

    delay(1000L)
}

class RunChecker714: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}