/* 
{"deadlock":false,"nFunctions":6,"nCoroutines":3,"nChannels":7,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 7 different channels
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
package org.example.altered.test70
import org.example.altered.test70.RunChecker70.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class Producer(private val channel: Channel<Int>) {
    suspend fun sendData(data: Int) {
        channel.send(data)
    }
}

class Consumer(private val channel: Channel<Int>) {
    suspend fun receiveData(): Int {
        return channel.receive()
    }
}

class Processor(private val input: Channel<Int>, private val output: Channel<Int>) {
    suspend fun process() {
        val data = input.receive()
        output.send(data * 2)
    }
}

val channel1 = Channel<Int>()
val channel2 = Channel<Int>()
val channel3 = Channel<Int>()
val channel4 = Channel<Int>()
val channel5 = Channel<Int>()
val channel6 = Channel<Int>()
val channel7 = Channel<Int>()

fun coroutineOne(producer: Producer) {
    GlobalScope.launch(pool) {
        producer.sendData(1)
        producer.sendData(2)
        producer.sendData(3)
    }
}

fun coroutineTwo(processor: Processor) {
    GlobalScope.launch(pool) {
        processor.process()
    }
}

fun coroutineThree(consumer: Consumer) {
    GlobalScope.launch(pool) {
        println(consumer.receiveData())
        println(consumer.receiveData())
        println(consumer.receiveData())
    }
}

fun main(): Unit = runBlocking(pool) {
    val producer = Producer(channel1)
    val processor1 = Processor(channel1, channel2)
    val processor2 = Processor(channel2, channel3)
    val consumer = Consumer(channel3)

    coroutineOne(producer)
    coroutineTwo(processor1)
    coroutineTwo(processor2)
    coroutineThree(consumer)

    delay(1000L) // Let coroutines finish
}

class RunChecker70: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}