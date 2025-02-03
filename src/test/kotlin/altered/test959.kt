/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":3,"nChannels":3,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 3 different channels
- 3 different coroutines
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
package org.example.altered.test959
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ProducerA(private val outputChannel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            outputChannel.send(i)
        }
        outputChannel.close()
    }
}

class ProducerB(private val outputChannel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            outputChannel.send(i * 10)
        }
        outputChannel.close()
    }
}

class Consumer(private val inputChannel1: Channel<Int>, private val inputChannel2: Channel<Int>, private val resultChannel: Channel<Int>) {

    suspend fun consume() {
        val value1 = inputChannel1.receive()
        val value2 = inputChannel2.receive()
        resultChannel.send(value1 + value2)
    }
}

fun main(): Unit= runBlocking {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()
    val resultChannel = Channel<Int>()

    val producerA = ProducerA(channelA)
    val producerB = ProducerB(channelB)
    val consumer = Consumer(channelA, channelB, resultChannel)

    launch { producerA.produce() }
    launch { producerB.produce() }
    launch { consumer.consume() }

    val result = resultChannel.receive()
    println("Result: $result")

    resultChannel.close()
}

class RunChecker959: RunCheckerBase() {
    override fun block() = main()
}