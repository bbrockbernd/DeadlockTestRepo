/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":3,"nChannels":3,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 3 different channels
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
package org.example.altered.test973
import org.example.altered.test973.RunChecker973.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(private val channel: Channel<Int>) {
    suspend fun produce(value: Int) {
        channel.send(value)
    }
}

class Consumer(private val channel: Channel<Int>) {
    suspend fun consume(): Int {
        return channel.receive()
    }
}

class Processor(
    private val channel1: Channel<Int>,
    private val channel2: Channel<Int>
) {
    suspend fun process() {
        val value = channel1.receive() * 2
        channel2.send(value)
    }
}

fun runProducer(producer: Producer, value: Int) = runBlocking(pool) {
    launch(pool) {
        producer.produce(value)
    }
}

fun runConsumer(consumer: Consumer): Int = runBlocking(pool) {
    var result = 0
    launch(pool) {
        result = consumer.consume()
    }
    return@runBlocking result
}

fun runProcessor(processor: Processor) = runBlocking(pool) {
    launch(pool) {
        processor.process()
    }
}

fun main(): Unit= runBlocking(pool) {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()

    val producer = Producer(channel1)
    val consumer1 = Consumer(channel2)
    val consumer2 = Consumer(channel2)
    val processor = Processor(channel1, channel3)

    launch(pool) {
        // Deadlock is created here due to the cyclic dependency
        runProducer(producer, 10)
        runProcessor(processor)
        val result1 = runConsumer(consumer1)
        println(result1)
    }

    launch(pool) {
        // Another coroutine consuming from channel
        val result2 = runConsumer(consumer2)
        println(result2)
    }
}

class RunChecker973: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}