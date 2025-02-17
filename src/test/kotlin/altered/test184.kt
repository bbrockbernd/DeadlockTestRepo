/* 
{"deadlock":false,"nFunctions":6,"nCoroutines":8,"nChannels":8,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 8 different channels
- 8 different coroutines
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
package org.example.altered.test184
import org.example.altered.test184.RunChecker184.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class ProducerA(val channel1: Channel<Int>, val channel2: Channel<Int>) {
    suspend fun produceA() {
        repeat(10) {
            channel1.send(it)
            channel2.send(it)
        }
    }
}

class ProducerB(val channel3: Channel<Int>, val channel4: Channel<Int>) {
    suspend fun produceB() {
        repeat(10) {
            channel3.send(it + 10)
            channel4.send(it + 10)
        }
    }
}

class ConsumerA(val channel5: Channel<Int>, val channel6: Channel<Int>) {
    suspend fun consumeA() {
        repeat(10) {
            println("ConsumerA received: ${channel5.receive()}, ${channel6.receive()}")
        }
    }
}

class ConsumerB(val channel7: Channel<Int>, val channel8: Channel<Int>) {
    suspend fun consumeB() {
        repeat(10) {
            println("ConsumerB received: ${channel7.receive()}, ${channel8.receive()}")
        }
    }
}

class Coordinator(
    val producerA: ProducerA,
    val producerB: ProducerB,
    val consumerA: ConsumerA,
    val consumerB: ConsumerB
) {
    suspend fun coordinate() = coroutineScope {
        launch(pool) { producerA.produceA() }
        launch(pool) { producerB.produceB() }
        launch(pool) { consumerA.consumeA() }
        launch(pool) { consumerB.consumeB() }
    }
}

suspend fun orchestrateChannels() = coroutineScope {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()
    val channel6 = Channel<Int>()
    val channel7 = Channel<Int>()
    val channel8 = Channel<Int>()

    val producerA = ProducerA(channel1, channel2)
    val producerB = ProducerB(channel3, channel4)
    val consumerA = ConsumerA(channel5, channel6)
    val consumerB = ConsumerB(channel7, channel8)

    launch(pool) { channelBridge(channel1, channel5) }
    launch(pool) { channelBridge(channel2, channel6) }
    launch(pool) { channelBridge(channel3, channel7) }
    launch(pool) { channelBridge(channel4, channel8) }

    val coordinator = Coordinator(producerA, producerB, consumerA, consumerB)
    coordinator.coordinate()
}

suspend fun channelBridge(input: Channel<Int>, output: Channel<Int>) {
    repeat(10) {
        output.send(input.receive())
    }
}

fun main(): Unit= runBlocking(pool) {
    orchestrateChannels()
}

class RunChecker184: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}