/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":7,"nChannels":3,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 3 different channels
- 7 different coroutines
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
package org.example.altered.test164
import org.example.altered.test164.RunChecker164.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ProducerA(val channel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..3) {
            channel.send(i) // Producing 1, 2, 3
        }
    }
}

class ProducerB(val channel: Channel<Int>) {
    suspend fun produce() {
        for (i in 4..6) {
            channel.send(i) // Producing 4, 5, 6
        }
    }
}

class ConsumerA(val channel: Channel<Int>) {
    suspend fun consume(): Int {
        var sum = 0
        for (i in 1..3) {
            sum += channel.receive() // Consuming 3 items
        }
        return sum
    }
}

class IntermediateConsumer(val inputChannel: Channel<Int>, val outputChannel: Channel<Int>) {
    suspend fun process() {
        for (i in 1..3) {
            val value = inputChannel.receive() // Receiving from ProducerA
            outputChannel.send(value * 2) // Sending modified values to ProducerB
        }
    }
}

class ConsumerB(val channel: Channel<Int>) {
    suspend fun consume(): Int {
        var product = 1
        for (i in 1..3) {
            product *= channel.receive() // Consuming 3 items
        }
        return product
    }
}

fun mainFunction() = runBlocking(pool) {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()

    val producerA = ProducerA(channel1)
    val producerB = ProducerB(channel3)
    val consumerA = ConsumerA(channel2)
    val intermediateConsumer = IntermediateConsumer(channel1, channel2)
    val consumerB = ConsumerB(channel3)

    launch(pool) { producerA.produce() }
    launch(pool) { intermediateConsumer.process() }
    launch(pool) { producerB.produce() }
    launch(pool) {
        val resultA = consumerA.consume()
        println("Consumer A Consumed Sum: $resultA")
    }
    launch(pool) {
        val resultB = consumerB.consume()
        println("Consumer B Consumed Product: $resultB")
    }

    // These additional coroutines add to the complexity and potential deadlocks
    launch(pool) { producerA.produce() }
    launch(pool) { producerB.produce() }
}
fun main(): Unit{
    mainFunction()
}

class RunChecker164: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}