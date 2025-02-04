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
package org.example.altered.test860
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ProducerA(private val channel1: Channel<Int>) {
    suspend fun produce() {
        repeat(3) {
            channel1.send(it)
            println("ProducerA sent: $it")
        }
    }
}

class ProducerB(private val channel2: Channel<String>) {
    suspend fun produce() {
        repeat(3) {
            val message = "Message $it"
            channel2.send(message)
            println("ProducerB sent: $message")
        }
    }
}

class Consumer(private val channel1: Channel<Int>, private val channel2: Channel<String>) {
    suspend fun consume() {
        repeat(3) {
            val item = channel1.receive()
            println("Consumer received from ProducerA: $item")
            val message = channel2.receive()
            println("Consumer received from ProducerB: $message")
        }
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<String>()

    val producerA = ProducerA(channel1)
    val producerB = ProducerB(channel2)
    val consumer = Consumer(channel1, channel2)

    // Coroutines causing deliberate deadlock
    launch { producerA.produce() }
    launch { consumer.consume() }
    launch { producerB.produce() }
    launch { consumer.consume() }
    launch { producerB.produce() }
}

class RunChecker860: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}