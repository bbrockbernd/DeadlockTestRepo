/* 
{"deadlock":false,"nFunctions":6,"nCoroutines":3,"nChannels":3,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 3 different channels
- 3 different coroutines
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
package org.example.altered.test195
import org.example.altered.test195.RunChecker195.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(val channel: Channel<Int>) {
    suspend fun produceData() {
        for (i in 1..5) {
            channel.send(i)
        }
    }
}

class ConsumerA(val channel: Channel<Int>) {
    suspend fun consumeDataA(): List<Int> {
        val data = mutableListOf<Int>()
        repeat(2) {
            data.add(channel.receive())
        }
        return data
    }
}

class ConsumerB(val channel: Channel<Int>) {
    suspend fun consumeDataB(): List<Int> {
        val data = mutableListOf<Int>()
        repeat(3) {
            data.add(channel.receive())
        }
        return data
    }
}

class Processor {
    fun process(data: List<Int>): List<Int> {
        return data.map { it * 2 }
    }
}

class Aggregator {
    fun aggregate(dataA: List<Int>, dataB: List<Int>): List<Int> {
        return dataA + dataB
    }
}

class ResultPrinter {
    fun printResult(data: List<Int>) {
        println("Result: $data")
    }
}

fun main(): Unit= runBlocking(pool) {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>(2)
    val channel3 = Channel<Int>(2)

    val producer = Producer(channel1)
    val consumerA = ConsumerA(channel1)
    val consumerB = ConsumerB(channel1)
    val processor = Processor()
    val aggregator = Aggregator()
    val resultPrinter = ResultPrinter()

    // Coroutine 1: Producing data
    launch(pool) {
        producer.produceData()
    }

    // Coroutine 2: Consuming and processing data from Channel 1 to Channel 2
    launch(pool) {
        val dataA = consumerA.consumeDataA()
        val processedDataA = processor.process(dataA)
        for (item in processedDataA) {
            channel2.send(item)
        }
    }

    // Coroutine 3: Consuming and processing data from Channel 2 to Channel 3
    launch(pool) {
        val dataB = consumerB.consumeDataB()
        val processedDataB = processor.process(dataB)
        for (item in processedDataB) {
            channel3.send(item)
        }
    }

    // Collecting results
    val resultA = processor.process((1..2).map { channel2.receive() })
    val resultB = processor.process((1..3).map { channel3.receive() })
    
    val finalResult = aggregator.aggregate(resultA, resultB)
    resultPrinter.printResult(finalResult)
}

class RunChecker195: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}