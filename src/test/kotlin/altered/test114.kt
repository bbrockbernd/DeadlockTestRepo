/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":2,"nChannels":1,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 1 different channels
- 2 different coroutines
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
package org.example.altered.test114
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(private val channel: Channel<Int>) {
    fun produce() = runBlocking {
        channel.send(generateValue())
        channel.close() // finish producing
    }

    private fun generateValue() = 42
}

class Consumer(private val channel: Channel<Int>) {
    fun consume() = runBlocking {
        val value = channel.receive()
        println("Consumed: $value")
    }
}

fun coroutineProducer(channel: Channel<Int>) = runBlocking {
    launch {
        val producer = Producer(channel)
        producer.produce()
    }
}

fun coroutineConsumer(channel: Channel<Int>) = runBlocking {
    launch {
        val consumer = Consumer(channel)
        consumer.consume()
    }
}

fun main(): Unit{
    val channel = Channel<Int>()
    coroutineProducer(channel)
    coroutineConsumer(channel)
}

class RunChecker114: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}