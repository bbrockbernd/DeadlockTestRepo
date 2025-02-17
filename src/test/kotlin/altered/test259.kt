/* 
{"deadlock":false,"nFunctions":6,"nCoroutines":2,"nChannels":6,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 6 different channels
- 2 different coroutines
- 4 different classes

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
package org.example.altered.test259
import org.example.altered.test259.RunChecker259.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class ProducerA(val channel: Channel<Int>)
class ProducerB(val channel: Channel<Int>)
class ConsumerA(val channel: Channel<Int>)
class ConsumerB(val channel: Channel<Int>)

fun produceA(channel: Channel<Int>) = runBlocking(pool) {
    launch(pool) {
        for (x in 1..5) channel.send(x)
        channel.close()
    }
}

fun produceB(channel: Channel<Int>) = runBlocking(pool) {
    launch(pool) {
        for (x in 6..10) channel.send(x)
        channel.close()
    }
}

fun consumeA(channel: Channel<Int>) = runBlocking(pool) {
    launch(pool) {
        for (x in channel) println("Consumer A received: $x")
    }
}

fun consumeB(channel: Channel<Int>) = runBlocking(pool) {
    launch(pool) {
        for (x in channel) println("Consumer B received: $x")
    }
}

fun main(): Unit = runBlocking(pool) {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    
    val producerA = ProducerA(channel1)
    val producerB = ProducerB(channel2)
    val consumerA = ConsumerA(channel1)
    val consumerB = ConsumerB(channel2)
    
    launch(pool) { produceA(producerA.channel) }
    launch(pool) { produceB(producerB.channel) }
    launch(pool) { consumeA(consumerA.channel) }
    launch(pool) { consumeB(consumerB.channel) }
}

class RunChecker259: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}