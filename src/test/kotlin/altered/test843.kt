/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":1,"nChannels":1,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 1 different channels
- 1 different coroutines
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
package org.example.altered.test843
import org.example.altered.test843.RunChecker843.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer {
    private val channel = Channel<Int>()

    fun getChannel() = channel

    suspend fun produce() {
        channel.send(1)
        channel.send(2)
        channel.close()
    }
}

class Processor(private val producer: Producer) {
    private val channel = producer.getChannel()

    suspend fun process() {
        for (value in channel) {
            println("Processed: $value")
        }
    }
}

class Consumer(private val processor: Processor) {
    suspend fun consume() {
        processor.process()
    }
}

suspend fun produceData(producer: Producer) {
    producer.produce()
}

suspend fun processData(processor: Processor) {
    processor.process()
}

fun main(): Unit= runBlocking(pool) {
    val producer = Producer()
    val processor = Processor(producer)
    val consumer = Consumer(processor)

    launch(pool) {
        produceData(producer)
    }

    launch(pool) {
        processData(processor)
    }

    consumer.consume()
}

class RunChecker843: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}