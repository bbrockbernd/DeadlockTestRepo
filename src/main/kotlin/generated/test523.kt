/* 
{"deadlock":false,"nFunctions":1,"nCoroutines":2,"nChannels":4,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
- 4 different channels
- 2 different coroutines
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
package org.example.generated.test523
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class ProducerA(private val outputChannel: SendChannel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            outputChannel.send(i)
            delay(100)
        }
    }
}

class ProducerB(private val outputChannel: SendChannel<Int>) {
    suspend fun produce() {
        for (i in 6..10) {
            outputChannel.send(i)
            delay(100)
        }
    }
}

class Consumer(private val inputChannelA: ReceiveChannel<Int>, private val inputChannelB: ReceiveChannel<Int>) {
    suspend fun consume() {
        repeat(5) {
            println("Consumer received from A: ${inputChannelA.receive()}")
            println("Consumer received from B: ${inputChannelB.receive()}")
        }
    }
}

fun main(): Unit= runBlocking {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()
    val channelC = Channel<Int>()
    val channelD = Channel<Int>()

    val producerA = ProducerA(channelA)
    val producerB = ProducerB(channelB)
    val consumer = Consumer(channelA, channelB)

    launch {
        producerA.produce()
    }
    launch {
        producerB.produce()
    }

    consumer.consume()
}