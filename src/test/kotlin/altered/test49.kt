/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":5,"nChannels":7,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 7 different channels
- 5 different coroutines
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
package org.example.altered.test49
import org.example.altered.test49.RunChecker49.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()

    suspend fun produceA() {
        for (i in 1..5) {
            channelA.send(i)
        }
        channelA.close()
    }

    suspend fun produceB() {
        for (i in 6..10) {
            channelB.send(i)
        }
        channelB.close()
    }
}

class Consumer {
    val channelC = Channel<Int>()
    val channelD = Channel<Int>()

    suspend fun consumeA(channel: Channel<Int>) {
        for (i in channel) {
            channelC.send(i * 2)
        }
        channelC.close()
    }

    suspend fun consumeB(channel: Channel<Int>) {
        for (i in channel) {
            channelD.send(i * 2)
        }
        channelD.close()
    }
}

class Aggregator {
    val channelE = Channel<Int>()
    suspend fun aggregate(channel1: Channel<Int>, channel2: Channel<Int>) {
        coroutineScope {
            launch(pool) {
                for (i in channel1) {
                    channelE.send(i)
                }
            }
            launch(pool) {
                for (i in channel2) {
                    channelE.send(i)
                }
            }
        }
        channelE.close()
    }
}

fun main(): Unit= runBlocking(pool) {
    val producer = Producer()
    val consumer = Consumer()
    val aggregator = Aggregator()

    val channelF = Channel<Int>()
    val channelG = Channel<Int>()

    launch(pool) {
        producer.produceA()
    }
    launch(pool) {
        producer.produceB()
    }
    launch(pool) {
        consumer.consumeA(producer.channelA)
    }
    launch(pool) {
        consumer.consumeB(producer.channelB)
    }
    launch(pool) {
        aggregator.aggregate(consumer.channelC, consumer.channelD)
    }

    launch(pool) {
        for (i in aggregator.channelE) {
            channelF.send(i + 1)
        }
        channelF.close()
    }

    launch(pool) {
        for (i in channelF) {
            channelG.send(i * 3)
        }
        channelG.close()
    }

    for (i in channelG) {
        println(i)
    }
}

class RunChecker49: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}