/* 
{"deadlock":false,"nFunctions":7,"nCoroutines":4,"nChannels":1,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 1 different channels
- 4 different coroutines
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
package org.example.altered.test50
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer1(private val channel: Channel<Int>) {
    suspend fun produce() {
        for (x in 1..5) {
            channel.send(x)
        }
    }
}

class Producer2(private val channel: Channel<Int>) {
    suspend fun produce() {
        for (x in 6..10) {
            channel.send(x)
        }
    }
}

class Consumer1(private val channel: Channel<Int>) {
    suspend fun consume() {
        repeat(5) {
            val received = channel.receive()
            println("Consumer1 received: $received")
        }
    }
}

class Consumer2(private val channel: Channel<Int>) {
    suspend fun consume() {
        repeat(5) {
            val received = channel.receive()
            println("Consumer2 received: $received")
        }
    }
}

suspend fun runProducers(channel: Channel<Int>) {
    val producer1 = Producer1(channel)
    val producer2 = Producer2(channel)
    coroutineScope {
        launch { producer1.produce() }
        launch { producer2.produce() }
    }
}

suspend fun runConsumers(channel: Channel<Int>) {
    val consumer1 = Consumer1(channel)
    val consumer2 = Consumer2(channel)
    coroutineScope {
        launch { consumer1.consume() }
        launch { consumer2.consume() }
    }
}

fun main(): Unit= runBlocking {
    val channel = Channel<Int>()
    launch { runProducers(channel) }
    launch { runConsumers(channel) }
    delay(1000L) // Wait for coroutines to finish
    channel.close()
}

class RunChecker50: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}