/* 
{"deadlock":false,"nFunctions":6,"nCoroutines":5,"nChannels":6,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 6 different channels
- 5 different coroutines
- 5 different classes

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
package org.example.generated.test30
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ProducerA(private val output: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            output.send(i)
        }
        output.close()
    }
}

class ProducerB(private val output: Channel<Int>) {
    suspend fun produce() {
        for (i in 6..10) {
            output.send(i)
        }
        output.close()
    }
}

class ConsumerA(private val input: Channel<Int>) {
    suspend fun consume() {
        for (x in input) {
            println("ConsumerA received: $x")
        }
    }
}

class ConsumerB(private val input: Channel<Int>) {
    suspend fun consume() {
        for (x in input) {
            println("ConsumerB received: $x")
        }
    }
}

class Coordinator {
    private val channel1 = Channel<Int>()
    private val channel2 = Channel<Int>()
    private val channel3 = Channel<Int>()
    private val channel4 = Channel<Int>()
    private val channel5 = Channel<Int>()
    private val channel6 = Channel<Int>()

    suspend fun init() {
        val producerA = ProducerA(channel1)
        val producerB = ProducerB(channel2)
        val consumerA = ConsumerA(channel3)
        val consumerB = ConsumerB(channel4)

        coroutineScope {
            launch { producerA.produce() }
            launch { producerB.produce() }
            launch { relay(channel1, channel3, channel5) }
            launch { relay(channel2, channel4, channel6) }
            launch { consumerA.consume() }
            launch { consumerB.consume() }
        }
    }

    private suspend fun relay(input: Channel<Int>, output1: Channel<Int>, output2: Channel<Int>) {
        for (x in input) {
            output1.send(x)
            output2.send(x)
        }
        output1.close()
        output2.close()
    }
}

suspend fun main(): Unit= coroutineScope {
    val coordinator = Coordinator()
    coordinator.init()
}