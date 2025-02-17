/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":2,"nChannels":1,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 1 different channels
- 2 different coroutines
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
package org.example.altered.test367
import org.example.altered.test367.RunChecker367.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Sender(private val channel: Channel<Int>) {
    suspend fun sendNumber(number: Int) {
        channel.send(number)
    }
}

class Receiver(private val channel: Channel<Int>) {
    suspend fun receiveNumber(): Int {
        return channel.receive()
    }
}

class Processor {
    fun process(number: Int): Int {
        return number * 2
    }
}

class Logger {
    fun log(message: String) {
        println(message)
    }
}

class Coordinator(
    private val sender: Sender,
    private val receiver: Receiver,
    private val processor: Processor,
    private val logger: Logger
) {
    suspend fun coordinate() {
        for (i in 1..5) {
            sender.sendNumber(i)
            val receivedNumber = receiver.receiveNumber()
            val processedNumber = processor.process(receivedNumber)
            logger.log("Processed Number: $processedNumber")
        }
    }
}

fun main(): Unit= runBlocking(pool) {
    val channel = Channel<Int>()

    val sender = Sender(channel)
    val receiver = Receiver(channel)
    val processor = Processor()
    val logger = Logger()
    val coordinator = Coordinator(sender, receiver, processor, logger)

    launch(pool) {
        coordinator.coordinate()
    }

    launch(pool) {
        while (!channel.isClosedForReceive) {
            val receivedNumber = receiver.receiveNumber()
            val processedNumber = processor.process(receivedNumber)
            logger.log("Received and Processed Number: $processedNumber")
        }
    }
}

class RunChecker367: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}