/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":5,"nChannels":4,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 4 different channels
- 5 different coroutines
- 2 different classes

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
package org.example.altered.test530
import org.example.altered.test530.RunChecker530.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ProducerA {
    val channel = Channel<Int>()

    suspend fun produce() {
        for (i in 1..5) {
            channel.send(i)
            delay(100)
        }
        channel.close()
    }
}

class ProducerB {
    val channel = Channel<Int>()

    suspend fun produce() {
        for (i in 6..10) {
            channel.send(i)
            delay(100)
        }
        channel.close()
    }
}

fun processA(inputChannel: Channel<Int>, outputChannel: Channel<Int>) = runBlocking(pool) {
    launch(pool) {
        for (received in inputChannel) {
            outputChannel.send(received * 2)
        }
        outputChannel.close()
    }
}

fun processB(inputChannel: Channel<Int>, outputChannel: Channel<Int>) = runBlocking(pool) {
    launch(pool) {
        for (received in inputChannel) {
            outputChannel.send(received + 2)
        }
        outputChannel.close()
    }
}

fun main(): Unit= runBlocking(pool) {
    val producerA = ProducerA()
    val producerB = ProducerB()
    val channelAtoB = Channel<Int>()
    val channelBtoA = Channel<Int>()

    launch(pool) { producerA.produce() }
    launch(pool) { producerB.produce() }
    launch(pool) { processA(producerA.channel, channelAtoB) }
    launch(pool) { processB(producerB.channel, channelBtoA) }

    launch(pool) {
        for (received in channelAtoB) {
            println("Processed by A: $received")
        }
    }

    launch(pool) {
        for (received in channelBtoA) {
            println("Processed by B: $received")
        }
    }
}

class RunChecker530: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}