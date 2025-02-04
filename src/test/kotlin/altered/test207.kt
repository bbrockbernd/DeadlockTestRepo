/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":8,"nChannels":1,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 1 different channels
- 8 different coroutines
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
package org.example.altered.test207
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(private val channel: Channel<Int>) {
    suspend fun sendValues() {
        for (i in 1..4) {
            channel.send(i)
            delay(100)
        }
    }
}

class Consumer(private val channel: Channel<Int>) {
    suspend fun receiveValues(): List<Int> {
        val receivedValues = mutableListOf<Int>()
        repeat(4) {
            val value = channel.receive()
            receivedValues.add(value)
        }
        return receivedValues
    }
}

class Processor(private val consumer: Consumer) {
    suspend fun processValues(): List<Int> {
        val values = consumer.receiveValues()
        return values.map { it * 2 }
    }
}

class Printer {
    fun printValues(values: List<Int>) {
        for (value in values) {
            println(value)
        }
    }
}

fun main(): Unit= runBlocking {
    val channel = Channel<Int>()
    val producer = Producer(channel)
    val consumer = Consumer(channel)
    val processor = Processor(consumer)
    val printer = Printer()

    launch { producer.sendValues() }
    launch { producer.sendValues() }
    launch { producer.sendValues() }
    launch { producer.sendValues() }

    launch {
        val processedValues = processor.processValues()
        printer.printValues(processedValues)
    }
    launch {
        val processedValues = processor.processValues()
        printer.printValues(processedValues)
    }
    launch {
        val processedValues = processor.processValues()
        printer.printValues(processedValues)
    }
    launch {
        val processedValues = processor.processValues()
        printer.printValues(processedValues)
    }
}

class RunChecker207: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}