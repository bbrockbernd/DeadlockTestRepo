/* 
{"deadlock":false,"nFunctions":1,"nCoroutines":4,"nChannels":4,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
- 4 different channels
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
package org.example.altered.test646
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(private val channel: Channel<Int>) {
    suspend fun produce() {
        repeat(10) {
            delay(100L)
            channel.send(it)
        }
    }
}

class Transformer(private val input: Channel<Int>, private val output: Channel<Int>) {
    suspend fun transform() {
        for (x in input) {
            delay(150L)
            output.send(x * 2)
        }
    }
}

class Consumer(private val channel: Channel<Int>) {
    suspend fun consume() {
        for (y in channel) {
            delay(200L)
            println("Consumed: $y")
        }
    }
}

fun producerJob(channel: Channel<Int>): Job {
    return GlobalScope.launch {
        val producer = Producer(channel)
        producer.produce()
    }
}

fun transformerJob(input: Channel<Int>, output: Channel<Int>): Job {
    return GlobalScope.launch {
        val transformer = Transformer(input, output)
        transformer.transform()
    }
}

fun consumerJob(channel: Channel<Int>): Job {
    return GlobalScope.launch {
        val consumer = Consumer(channel)
        consumer.consume()
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()

    val job1 = producerJob(channel1)
    val job2 = transformerJob(channel1, channel2)
    val job3 = transformerJob(channel2, channel3)
    val job4 = consumerJob(channel3)

    joinAll(job1, job2, job3, job4)
}

class RunChecker646: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}