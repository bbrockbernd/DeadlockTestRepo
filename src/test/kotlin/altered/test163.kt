/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":5,"nChannels":3,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 3 different channels
- 5 different coroutines
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
package org.example.altered.test163
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(val name: String, val output: Channel<Int>) {
    suspend fun produce() {
        repeat(5) {
            output.send(it)
            println("$name produced $it")
        }
    }
}

class Consumer(val name: String, val input: Channel<Int>) {
    suspend fun consume() {
        repeat(5) {
            val value = input.receive()
            println("$name consumed $value")
        }
    }
}

class Intermediate(val input: Channel<Int>, val output: Channel<Int>) {
    suspend fun filterAndForward() {
        repeat(5) {
            val value = input.receive()
            if (value % 2 == 0) {
                output.send(value)
                println("Forwarded $value")
            }
        }
    }
}

class Printer(val input: Channel<Int>) {
    suspend fun print() {
        repeat(5) {
            val value = input.receive()
            println("Printed $value")
        }
    }
}

class Summarizer(val input: Channel<Int>) {
    suspend fun summarize() {
        var sum = 0
        repeat(5) {
            sum += input.receive()
        }
        println("Total sum is $sum")
    }
}

fun runProducer(producer: Producer) {
    GlobalScope.launch {
        producer.produce()
    }
}

fun runConsumer(consumer: Consumer) {
    GlobalScope.launch {
        consumer.consume()
    }
}

fun main(): Unit{
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    
    val producer = Producer("Producer1", channel1)
    val consumer = Consumer("Consumer1", channel3)
    val intermediate = Intermediate(channel1, channel2)
    val printer = Printer(channel2)
    val summarizer = Summarizer(channel3)

    runBlocking {
        runProducer(producer)
        runConsumer(consumer)
        
        launch { intermediate.filterAndForward() }
        launch { printer.print() }
        launch { summarizer.summarize() }
    }
}

class RunChecker163: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}