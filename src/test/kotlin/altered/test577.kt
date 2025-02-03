/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":3,"nChannels":5,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 5 different channels
- 3 different coroutines
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
package org.example.altered.test577
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()
    
    suspend fun produceA() {
        for (i in 1..5) {
            channelA.send(i)
        }
    }

    suspend fun produceB() {
        for (i in 6..10) {
            channelB.send(i)
        }
    }
}

class Consumer {
    val channelC = Channel<Int>()
    
    suspend fun consumeC(channel: Channel<Int>) {
        for (i in 1..5) {
            val value = channel.receive()
            channelC.send(value)
        }
    }

    suspend fun forwardToD(channel: Channel<Int>, channelD: Channel<Int>) {
        for (i in 1..5) {
            val value = channel.receive()
            channelD.send(value)
        }
    }
}

fun main(): Unit= runBlocking {
    val producer = Producer()
    val consumer = Consumer()
    val channelE = Channel<Int>()
    
    launch {
        producer.produceA()
    }
    
    launch {
        consumer.consumeC(producer.channelA)
    }
    
    launch {
        consumer.forwardToD(consumer.channelC, channelE)
    }

    // Introducing deadlock by attempting to produceB without consuming its output
    launch {
        producer.produceB()
    }
}

class RunChecker577: RunCheckerBase() {
    override fun block() = main()
}