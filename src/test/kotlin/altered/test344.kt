/* 
{"deadlock":false,"nFunctions":7,"nCoroutines":2,"nChannels":8,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 8 different channels
- 2 different coroutines
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
package org.example.altered.test344
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()
    val channelC = Channel<Int>()
    val channelD = Channel<Int>()

    fun produceA() {
        GlobalScope.launch {
            for (i in 1..5) {
                channelA.send(i)
            }
            channelA.close()
        }
    }

    fun produceB() {
        GlobalScope.launch {
            for (i in 6..10) {
                channelB.send(i)
            }
            channelB.close()
        }
    }
}

class Consumer {
    val channelE = Channel<Int>()
    val channelF = Channel<Int>()
    val channelG = Channel<Int>()
    val channelH = Channel<Int>()

    suspend fun consumeA(channelA: Channel<Int>) {
        for (i in channelA) {
            channelE.send(i * 2)
        }
        channelE.close()
    }

    suspend fun consumeB(channelB: Channel<Int>) {
        for (i in channelB) {
            channelF.send(i * 2)
        }
        channelF.close()
    }

    fun consumeC(channelE: Channel<Int>, channelG: Channel<Int>) {
        GlobalScope.launch {
            for (i in channelE) {
                channelG.send(i + 1)
            }
            channelG.close()
        }
    }

    fun consumeD(channelF: Channel<Int>, channelH: Channel<Int>) {
        GlobalScope.launch {
            for (i in channelF) {
                channelH.send(i + 1)
            }
            channelH.close()
        }
    }
}

fun initiateProducers(producer: Producer) {
    producer.produceA()
    producer.produceB()
}

fun initiateConsumers(consumer: Consumer, producer: Producer) = runBlocking {
    consumer.consumeA(producer.channelA)
    consumer.consumeB(producer.channelB)
    consumer.consumeC(consumer.channelE, consumer.channelG)
    consumer.consumeD(consumer.channelF, consumer.channelH)
}

fun main(): Unit{
    val producer = Producer()
    val consumer = Consumer()
    runBlocking {
        initiateProducers(producer)
        initiateConsumers(consumer, producer)
    }
}

class RunChecker344: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}