/* 
{"deadlock":false,"nFunctions":1,"nCoroutines":3,"nChannels":3,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
- 3 different channels
- 3 different coroutines
- 2 different classes

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
package org.example.altered.test968
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer {
    val channel = Channel<Int>()
    suspend fun produce() {
        for (i in 1..5) {
            channel.send(i)
        }
        channel.close()
    }
}

class Consumer {
    val channel = Channel<Int>()
    suspend fun consume(input: Channel<Int>) {
        for (i in input) {
            channel.send(input.receive())
        }
        channel.close()
    }
}

fun handle(channel1: Channel<Int>, channel2: Channel<Int>) = runBlocking {
    for (i in channel2) {
        println(i)
    }
}

fun main(): Unit= runBlocking {
    val producer = Producer()
    val consumer = Consumer()
    val printer = Channel<Int>()

    launch { producer.produce() }
    launch { consumer.consume(producer.channel) }
    launch { handle(consumer.channel, printer) }

    for (i in consumer.channel) {
        printer.send(i)
    }

    printer.close()
}

class RunChecker968: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}