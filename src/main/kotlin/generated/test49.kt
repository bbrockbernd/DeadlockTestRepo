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
package org.example.generated.test49
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
            launch {
                for (i in channel1) {
                    channelE.send(i)
                }
            }
            launch {
                for (i in channel2) {
                    channelE.send(i)
                }
            }
        }
        channelE.close()
    }
}

fun main(): Unit= runBlocking {
    val producer = Producer()
    val consumer = Consumer()
    val aggregator = Aggregator()

    val channelF = Channel<Int>()
    val channelG = Channel<Int>()

    launch {
        producer.produceA()
    }
    launch {
        producer.produceB()
    }
    launch {
        consumer.consumeA(producer.channelA)
    }
    launch {
        consumer.consumeB(producer.channelB)
    }
    launch {
        aggregator.aggregate(consumer.channelC, consumer.channelD)
    }

    launch {
        for (i in aggregator.channelE) {
            channelF.send(i + 1)
        }
        channelF.close()
    }

    launch {
        for (i in channelF) {
            channelG.send(i * 3)
        }
        channelG.close()
    }

    for (i in channelG) {
        println(i)
    }
}