/* 
{"deadlock":false,"nFunctions":8,"nCoroutines":8,"nChannels":4,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
- 4 different channels
- 8 different coroutines
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
package org.example.altered.test236
import org.example.altered.test236.RunChecker236.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class ProducerA(val outputChannel: SendChannel<Int>) {
    suspend fun produceA() {
        for (i in 1..5) {
            outputChannel.send(i)
        }
    }
}

class ProducerB(val outputChannel: SendChannel<String>) {
    suspend fun produceB() {
        for (i in 1..5) {
            outputChannel.send("Text $i")
        }
    }
}

class ConsumerA(val inputChannel: ReceiveChannel<Int>) {
    suspend fun consumeA() {
        for (i in 1..5) {
            println("ConsumerA received: ${inputChannel.receive()}")
        }
    }
}

fun logMessage(messageChannel: ReceiveChannel<String>) = runBlocking(pool) {
    launch(pool) {
        for (i in 1..5) {
            println("Log: ${messageChannel.receive()}")
        }
    }
}

fun runProducers(channel1: SendChannel<Int>, channel2: SendChannel<String>) = runBlocking(pool) {
    val producerA = ProducerA(channel1)
    val producerB = ProducerB(channel2)
    launch(pool) { producerA.produceA() }
    launch(pool) { producerB.produceB() }
}

fun runConsumers(channel1: ReceiveChannel<Int>, channel2: ReceiveChannel<String>) = runBlocking(pool) {
    val consumerA = ConsumerA(channel1)
    launch(pool) { consumerA.consumeA() }
    logMessage(channel2)
}

fun main(): Unit = runBlocking(pool) {
    val channel1 = Channel<Int>(5)
    val channel2 = Channel<String>(5)

    launch(pool) { runProducers(channel1, channel2) }
    launch(pool) { runConsumers(channel1, channel2) }
}

class RunChecker236: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}