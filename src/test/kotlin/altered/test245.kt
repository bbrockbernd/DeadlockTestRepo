/* 
{"deadlock":false,"nFunctions":7,"nCoroutines":4,"nChannels":7,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 7 different channels
- 4 different coroutines
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
package org.example.altered.test245
import org.example.altered.test245.RunChecker245.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Processor {
    val channel1 = Channel<Int>(Channel.BUFFERED)
    fun process(input: Int) = runBlocking(pool) {
        channel1.send(input)
    }
}

class Reader(private val channel: Channel<Int>) {
    suspend fun readAndPrint() {
        while (true) {
            val received = channel.receive()
            println("Received: $received")
        }
    }
}

class Writer(private val channel: Channel<Int>) {
    suspend fun writeData(data: Int) {
        channel.send(data)
    }
}

class Logger {
    val channel2 = Channel<String>(Channel.BUFFERED)
    suspend fun log(message: String) {
        channel2.send(message)
    }

    suspend fun logReader() {
        while (true) {
            val logMessage = channel2.receive()
            println("Log: $logMessage")
        }
    }
}

class Transformer {
    val channel3 = Channel<Double>(Channel.BUFFERED)
    fun transform(input: Double) = runBlocking(pool) {
        channel3.send(input * 2)
    }
}

class Aggregator {
    val channel4 = Channel<Pair<Int, Double>>(Channel.BUFFERED)
    suspend fun aggregate(intVal: Int, doubleVal: Double) {
        channel4.send(Pair(intVal, doubleVal))
    }
}

class FinalProcessor {
    val channel5 = Channel<String>(Channel.BUFFERED)
    suspend fun processFinal(pair: Pair<Int, Double>) {
        channel5.send("Final result: ${pair.first * pair.second}")
    }

    suspend fun finalProcessorReader() {
        while (true) {
            val result = channel5.receive()
            println(result)
        }
    }
}

fun main(): Unit= runBlocking(pool) {
    val processor = Processor()
    val reader = Reader(processor.channel1)
    val writerChannel = Channel<Int>(Channel.BUFFERED)
    val writer = Writer(writerChannel)
    val logger = Logger()
    val transformer = Transformer()
    val aggregator = Aggregator()
    val finalProcessor = FinalProcessor()

    launch(pool) { reader.readAndPrint() }
    launch(pool) { logger.logReader() }
    launch(pool) { finalProcessor.finalProcessorReader() }

    processor.process(42)
    logger.log("Processing: 42")

    writer.writeData(100)
    transformer.transform(13.5)
    
    aggregator.aggregate(100, 27.0)
    aggregator.channel4.receive().let {
        finalProcessor.processFinal(it)
    }
    
    delay(1000) // Give some time for coroutines to run
}

class RunChecker245: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}