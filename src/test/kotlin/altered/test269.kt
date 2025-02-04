/* 
{"deadlock":true,"nFunctions":6,"nCoroutines":7,"nChannels":4,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 4 different channels
- 7 different coroutines
- 4 different classes

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
package org.example.altered.test269
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ProducerA(private val channel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..3) {
            channel.send(i)
        }
    }
}

class ConsumerA(private val channel: Channel<Int>) {
    suspend fun consume() {
        for (i in 1..3) {
            println("ConsumerA received: ${channel.receive()}")
        }
    }
}

class ProducerB(private val channel: Channel<Int>) {
    suspend fun produce() {
        for (i in 4..6) {
            channel.send(i)
        }
    }
}

class ConsumerB(private val channel: Channel<Int>) {
    suspend fun consume() {
        for (i in 4..6) {
            println("ConsumerB received: ${channel.receive()}")
        }
    }
}

val channel1 = Channel<Int>()
val channel2 = Channel<Int>()
val channel3 = Channel<Int>()
val channel4 = Channel<Int>()

fun function1() {
    runBlocking {
        launch {
            val producerA = ProducerA(channel1)
            producerA.produce()
        }
    }
}

fun function2() {
    runBlocking {
        launch {
            val consumerA = ConsumerA(channel1)
            consumerA.consume()
        }
    }
}

fun function3() {
    runBlocking {
        launch {
            val producerB = ProducerB(channel2)
            producerB.produce()
        }
    }
}

fun function4() {
    runBlocking {
        launch {
            val consumerB = ConsumerB(channel2)
            consumerB.consume()
        }
    }
}

fun function5() {
    runBlocking {
        launch {
            for (i in 1..3) {
                channel3.send(i)
            }
        }
    }
}

fun function6() {
    runBlocking {
        launch {
            for (i in 1..3) {
                println("function6 received: ${channel3.receive()}")
            }
        }
    }
}

fun main(): Unit{
    runBlocking {
        launch {
            function1()  // ProducerA -> channel1
            function2()  // ConsumerA <- channel1
        }
        launch {
            function3()  // ProducerB -> channel2
            function4()  // ConsumerB <- channel2
        }
        launch {
            for (i in 7..10) {
                channel4.send(i) // Deadlock here, no receiver on channel4
            }
        }
        launch {
            delay(1000) // Force timeout to simulate attempted receive on channel4; never happens
        }
    }
    function5()  // Simple send on channel3
    function6()  // Simple receive on channel3
}

class RunChecker269: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}