/* 
{"deadlock":false,"nFunctions":7,"nCoroutines":7,"nChannels":3,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 3 different channels
- 7 different coroutines
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
package org.example.generated.test125
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer1(private val channel: Channel<Int>) {
    fun produce() {
        GlobalScope.launch {
            for (i in 1..5) {
                channel.send(i)
            }
        }
    }
}

class Producer2(private val channel: Channel<Int>) {
    fun produce() {
        GlobalScope.launch {
            for (i in 6..10) {
                channel.send(i)
            }
        }
    }
}

class Consumer1(private val channel: Channel<Int>) {
    fun consume() {
        GlobalScope.launch {
            repeat(5) {
                println("Consumer1 received: ${channel.receive()}")
            }
        }
    }
}

class Consumer2(private val channel: Channel<Int>) {
    fun consume() {
        GlobalScope.launch {
            repeat(5) {
                println("Consumer2 received: ${channel.receive()}")
            }
        }
    }
}

class Intermediate(private val inputChannel: Channel<Int>, private val outputChannel: Channel<Int>) {
    fun relay() {
        GlobalScope.launch {
            repeat(10) {
                outputChannel.send(inputChannel.receive())
            }
        }
    }
}

class Printer(private val channel: Channel<Int>) {
    fun print() {
        GlobalScope.launch {
            repeat(10) {
                println("Printer received: ${channel.receive()}")
            }
        }
    }
}

class Manager(
    private val producer1: Producer1,
    private val producer2: Producer2,
    private val consumer1: Consumer1,
    private val consumer2: Consumer2,
    private val intermediate: Intermediate,
    private val printer: Printer
) {
    fun start() {
        producer1.produce()
        producer2.produce()
        intermediate.relay()
        consumer1.consume()
        consumer2.consume()
        printer.print()
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()

    val producer1 = Producer1(channel1)
    val producer2 = Producer2(channel1)
    val consumer1 = Consumer1(channel2)
    val consumer2 = Consumer2(channel2)
    val intermediate = Intermediate(channel1, channel2)
    val printer = Printer(channel2)

    val manager = Manager(producer1, producer2, consumer1, consumer2, intermediate, printer)
    manager.start()

    delay(1000L)
}