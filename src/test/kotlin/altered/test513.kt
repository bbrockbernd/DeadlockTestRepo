/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":5,"nChannels":2,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 2 different channels
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
package org.example.altered.test513
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

// Classes
class ProducerA(val channel: Channel<Int>) {
    suspend fun produceA() {
        for (i in 1..5) {
            channel.send(i)
        }
    }
}

class ProducerB(val channel: Channel<Int>) {
    suspend fun produceB() {
        for (i in 6..10) {
            channel.send(i)
        }
    }
}

class ConsumerAB(val channelA: Channel<Int>, val channelB: Channel<Int>) {
    suspend fun consumeAB() {
        repeat(5) {
            val valueA = channelA.receive()
            println("Consumed from A: $valueA")
            val valueB = channelB.receive()
            println("Consumed from B: $valueB")
        }
    }
}

// Functions
suspend fun startProduceA(producerA: ProducerA) {
    producerA.produceA()
}

suspend fun startProduceB(producerB: ProducerB) {
    producerB.produceB()
}

suspend fun startConsumeAB(consumer: ConsumerAB) {
    consumer.consumeAB()
}

fun launchProducersAndConsumers() = runBlocking {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()

    val producerA = ProducerA(channelA)
    val producerB = ProducerB(channelB)
    val consumer = ConsumerAB(channelA, channelB)

    // Start coroutines
    launch { startProduceA(producerA) }
    launch { startProduceB(producerB) }
    launch { startConsumeAB(consumer) }
    
    // Additional coroutines that consume but do not produce, leading to potential deadlock
    launch { println("Additional consumer A: ${channelA.receive()}") }
    launch { println("Additional consumer B: ${channelB.receive()}") }
}

fun main(): Unit{
    launchProducersAndConsumers()
}

class RunChecker513: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}