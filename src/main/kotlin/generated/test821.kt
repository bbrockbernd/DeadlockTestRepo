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
package org.example.generated.test821
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ProducerA(private val output: Channel<Int>) {
    suspend fun produce() {
        output.send(1)
    }
}

class ProducerB(private val output: Channel<Int>) {
    suspend fun produce() {
        output.send(2)
    }
}

class Consumer(private val inputA: Channel<Int>, private val inputB: Channel<Int>, private val output: Channel<Int>) {
    suspend fun consume() {
        val a = inputA.receive()
        val b = inputB.receive()
        output.send(a + b)
    }
}

fun startProducers(producerA: ProducerA, producerB: ProducerB) {
    GlobalScope.launch {
        producerA.produce()
    }
    GlobalScope.launch {
        producerB.produce()
    }
}

fun startConsumer(consumer: Consumer) {
    GlobalScope.launch {
        consumer.consume()
    }
}

fun main(): Unit= runBlocking {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()
    val channelC = Channel<Int>()
    val channelD = Channel<Int>()

    val producerA = ProducerA(channelA)
    val producerB = ProducerB(channelB)
    val consumer = Consumer(channelA, channelB, channelC)

    startProducers(producerA, producerB)
    startConsumer(consumer)

    println(channelC.receive()) // should print 3, demonstrating no deadlock exists
}