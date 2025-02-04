/* 
{"deadlock":true,"nFunctions":8,"nCoroutines":3,"nChannels":1,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
- 1 different channels
- 3 different coroutines
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
package org.example.altered.test437
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(val channel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..3) {
            channel.send(i)
        }
    }
}

class Intermediate(private val channel: Channel<Int>) {
    suspend fun intermediateProcess() {
        val newChannel = Channel<Int>()
        GlobalScope.launch { process(newChannel) }
        for (i in 1..3) {
            val data = channel.receive()
            newChannel.send(data * 2)
        }
    }

    suspend fun process(channel: Channel<Int>) {
        for (i in 1..3) {
            channel.send(channel.receive() + 1)
        }
    }
}

class Consumer(private val channel: Channel<Int>) {
    suspend fun consume() {
        for (i in 1..3) {
            println("Consumed: ${channel.receive()}")
        }
    }
}

fun createProducer(channel: Channel<Int>) {
    GlobalScope.launch {
        val producer = Producer(channel)
        producer.produce()
    }
}

fun createIntermediate(channel: Channel<Int>) {
    GlobalScope.launch {
        val intermediate = Intermediate(channel)
        intermediate.intermediateProcess()
    }
}

fun createConsumer(channel: Channel<Int>) {
    GlobalScope.launch {
        val consumer = Consumer(channel)
        consumer.consume()
    }
}

fun mainLoop(channel: Channel<Int>) {
    runBlocking {
        createProducer(channel)
        createIntermediate(channel)
        createConsumer(channel)
        delay(5000)
    }
}

fun main(): Unit{
    val channel = Channel<Int>()
    mainLoop(channel)
} 

fun unnecessaryFunction() {
    println("This function doesn't do anything.")
}

fun anotherUnnecessaryFunction() {
    println("This function also doesn't do much.")
}

class RunChecker437: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}