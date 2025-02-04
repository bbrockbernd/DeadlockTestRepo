/* 
{"deadlock":false,"nFunctions":8,"nCoroutines":5,"nChannels":7,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
- 7 different channels
- 5 different coroutines
- 2 different classes

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
package org.example.altered.test263
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer1(private val channel: Channel<Int>) {
    suspend fun produce1() {
        for (i in 1..5) {
            channel.send(i)
        }
    }
}

class Producer2(private val channel: Channel<Int>) {
    suspend fun produce2() {
        for (i in 6..10) {
            channel.send(i)
        }
    }
}

fun consume1(channel: Channel<Int>) {
    runBlocking {
        repeat(3) {
            println("Consume1 received: ${channel.receive()}")
        }
    }
}

fun consume2(channel: Channel<Int>) {
    runBlocking {
        repeat(3) {
            println("Consume2 received: ${channel.receive()}")
        }
    }
}

fun bridgeChannels(channel1: Channel<Int>, channel2: Channel<Int>, channel3: Channel<Int>) {
    runBlocking {
        repeat(3) {
            val value1 = channel1.receive()
            channel3.send(value1)
            val value2 = channel2.receive()
            channel3.send(value2)
        }
    }
}

fun consumeFinal(channel: Channel<Int>) {
    runBlocking {
        repeat(6) {
            println("Final Consumer received: ${channel.receive()}")
        }
    }
}

suspend fun startProducers(producer1: Producer1, producer2: Producer2) {
    coroutineScope {
        launch { producer1.produce1() }
        launch { producer2.produce2() }
    }
}

fun main(): Unit{
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>(5)
    val producer1 = Producer1(channel1)
    val producer2 = Producer2(channel2)

    runBlocking {
        launch { startProducers(producer1, producer2) }
        launch { bridgeChannels(channel1, channel2, channel3) }
        launch { consume1(channel1) }
        launch { consume2(channel2) }
        launch { consumeFinal(channel3) }
    }
}

class RunChecker263: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}