/* 
{"deadlock":false,"nFunctions":6,"nCoroutines":3,"nChannels":6,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 6 different channels
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
package org.example.altered.test328
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer1(private val channel: Channel<Int>) {
    suspend fun produce() {
        repeat(5) {
            delay(100)
            channel.send(it)
        }
        channel.close()
    }
}

class Consumer1(private val channel: Channel<Int>) {
    suspend fun consume() {
        for (item in channel) {
            println("Consumer1: $item")
        }
    }
}

fun launchProducer1(ch: Channel<Int>) = GlobalScope.launch {
    val producer = Producer1(ch)
    producer.produce()
}

fun launchConsumer1(ch: Channel<Int>) = GlobalScope.launch {
    val consumer = Consumer1(ch)
    consumer.consume()
}

fun producerCoroutine2(ch: Channel<String>) = GlobalScope.launch {
    val items = listOf("A", "B", "C", "D", "E")
    for (item in items) {
        delay(150)
        ch.send(item)
    }
    ch.close()
}

fun consumerCoroutine2(ch: Channel<String>) = GlobalScope.launch {
    for (item in ch) {
        println("Consumer2: $item")
    }
}

fun main(): Unit = runBlocking {
    val channel1 = Channel<Int>(5)
    val channel2 = Channel<String>(5)

    val job1 = launchProducer1(channel1)
    val job2 = launchConsumer1(channel1)
    val job3 = producerCoroutine2(channel2)
    val job4 = consumerCoroutine2(channel2)

    joinAll(job1, job2, job3, job4) 
}

class RunChecker328: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}