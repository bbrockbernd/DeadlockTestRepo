/* 
{"deadlock":false,"nFunctions":7,"nCoroutines":6,"nChannels":6,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 6 different channels
- 6 different coroutines
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
package org.example.altered.test19
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class ProducerA {
    val channelA = Channel<Int>(1)
    val channelB = Channel<Int>(1)

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

class ConsumerB {
    val channelC = Channel<Int>(1)
    val channelD = Channel<Int>(1)

    suspend fun consumeA(channel: Channel<Int>) {
        for (x in channel) {
            channelC.send(x)
        }
        channelC.close()
    }

    suspend fun consumeB(channel: Channel<Int>) {
        for (x in channel) {
            channelD.send(x)
        }
        channelD.close()
    }

    suspend fun finalConsume() {
        for (x in channelC) {
            for (y in channelD) {
                println("Received $x and $y")
            }
        }
    }
}

suspend fun intermediateStepA(channelA: Channel<Int>, channelB: Channel<Int>) {
    for (x in channelA) {
        channelB.send(x * 2)
    }
    channelB.close()
}

suspend fun intermediateStepB(channel: Channel<Int>) {
    for (x in channel) {
        println("Intermediate step received $x")
    }
}

fun main(): Unit= runBlocking {
    val producerA = ProducerA()
    val consumerB = ConsumerB()
    val channelE = Channel<Int>(1)
    val channelF = Channel<Int>(1)

    launch {
        producerA.produceA()
    }
    launch {
        producerA.produceB()
    }
    launch {
        consumerB.consumeA(producerA.channelA)
    }
    launch {
        consumerB.consumeB(producerA.channelB)
    }
    launch {
        intermediateStepA(consumerB.channelC, channelE)
    }
    launch {
        intermediateStepB(channelE)
    }

    consumerB.finalConsume()
}

class RunChecker19: RunCheckerBase() {
    override fun block() = main()
}