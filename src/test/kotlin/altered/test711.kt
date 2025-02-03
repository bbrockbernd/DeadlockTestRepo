/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":4,"nChannels":5,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 5 different channels
- 4 different coroutines
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
package org.example.altered.test711
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class ProducerA(private val channel1: Channel<Int>, private val channel2: Channel<Int>) {
    suspend fun produce() {
        channel1.send(1)
        channel2.send(2)
    }
}

class ProducerB(private val channel3: Channel<Int>) {
    suspend fun produce() {
        channel3.send(3)
    }
}

class ConsumerA(private val channel4: Channel<Int>) {
    suspend fun consume() {
        val value = channel4.receive()
        println("ConsumerA received: $value")
    }
}

fun producerAFunction(channel1: Channel<Int>, channel2: Channel<Int>) = runBlocking {
    val producerA = ProducerA(channel1, channel2)
    launch {
        producerA.produce()
    }
}

fun producerBFunction(channel3: Channel<Int>) = runBlocking {
    val producerB = ProducerB(channel3)
    launch {
        producerB.produce()
    }
}

fun consumerAFunction(channel4: Channel<Int>) = runBlocking {
    val consumerA = ConsumerA(channel4)
    launch {
        consumerA.consume()
    }
}

fun initiatorFunction(channel1: Channel<Int>, channel2: Channel<Int>, channel3: Channel<Int>, channel4: Channel<Int>) = runBlocking {
    launch {
        val value1 = channel1.receive()
        channel4.send(value1)
    }
    launch {
        val value2 = channel2.receive()
        channel4.send(value2)
    }
    launch {
        val value3 = channel3.receive()
        channel4.send(value3)
    }
}

fun main(): Unit{
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()

    launch {
        producerAFunction(channel1, channel2)
    }
    launch {
        producerBFunction(channel3)
    }
    launch {
        consumerAFunction(channel4)
    }
    launch {
        initiatorFunction(channel1, channel2, channel3, channel4)
    }

    runBlocking {
        delay(1000L)
    }
}

class RunChecker711: RunCheckerBase() {
    override fun block() = main()
}