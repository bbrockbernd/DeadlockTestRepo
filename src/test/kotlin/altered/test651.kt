/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":3,"nChannels":5,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 5 different channels
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
package org.example.altered.test651
import org.example.altered.test651.RunChecker651.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(private val channel: Channel<Int>) {
    fun start() = GlobalScope.launch(pool) {
        for (i in 1..5) {
            channel.send(produceValue(i))
        }
        channel.close()
    }

    private fun produceValue(i: Int): Int {
        return i * 2
    }
}

class Consumer(private val channel: Channel<Int>, private val secondaryChannel: Channel<Int>) {
    fun start() = GlobalScope.launch(pool) {
        for (i in channel) {
            secondaryChannel.send(consumeValue(i))
        }
        secondaryChannel.close()
    }

    private fun consumeValue(value: Int): Int {
        return value / 2
    }
}

fun conversion(channel: Channel<Int>, secondaryChannel: Channel<Int>) = GlobalScope.launch(pool) {
    for (i in secondaryChannel) {
        channel.send(convert(i))
    }
    channel.close()
}

fun convert(value: Int): Int {
    return value + 3
}

fun finalProcessing(channel: Channel<Int>) = GlobalScope.launch(pool) {
    for (i in channel) {
        process(i)
    }
}

fun process(value: Int) {
    println("Processed value: $value")
}

fun main(): Unit= runBlocking(pool) {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()

    val producer = Producer(channel1)
    val consumer = Consumer(channel1, channel2)

    producer.start()
    consumer.start()
    conversion(channel3, channel2)
    conversion(channel4, channel3)
    finalProcessing(channel4)

    delay(1000) // Give enough time for coroutines to finish
}

class RunChecker651: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}