/* 
{"deadlock":true,"nFunctions":6,"nCoroutines":7,"nChannels":6,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 6 different channels
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
package org.example.generated.test133
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(val channel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            channel.send(i)
        }
    }
}

class ConsumerA(val channel: Channel<Int>) {
    suspend fun consume() {
        for (i in 1..2) {
            val value = channel.receive()
        }
    }
}

class ConsumerB(val channel: Channel<Int>) {
    suspend fun consume() {
        for (i in 1..2) {
            val value = channel.receive()
        }
    }
}

class ConnectorA(val inputChannel: Channel<Int>, val outputChannel: Channel<Int>) {
    suspend fun connect() {
        for (i in 1..2) {
            val value = inputChannel.receive()
            outputChannel.send(value)
        }
    }
}

class ConnectorB(val inputChannel: Channel<Int>, val outputChannel: Channel<Int>) {
    suspend fun connect() {
        for (i in 1..2) {
            val value = inputChannel.receive()
            outputChannel.send(value)
        }
    }
}

fun functionA(channel: Channel<Int>) {
    runBlocking {
        launch {
            Producer(channel).produce()
        }
    }
}

fun functionB(channel: Channel<Int>) {
    runBlocking {
        launch {
            ConsumerA(channel).consume()
        }
    }
}

fun functionC(channel: Channel<Int>) {
    runBlocking {
        launch {
            ConsumerB(channel).consume()
        }
    }
}

fun functionD(inputChannel: Channel<Int>, outputChannel: Channel<Int>) {
    runBlocking {
        launch {
            ConnectorA(inputChannel, outputChannel).connect()
        }
    }
}

fun functionE(inputChannel: Channel<Int>, outputChannel: Channel<Int>) {
    runBlocking {
        launch {
            ConnectorB(inputChannel, outputChannel).connect()
        }
    }
}

fun main(): Unit {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()
    val channel6 = Channel<Int>()

    runBlocking {
        launch { functionA(channel1) }
        launch { functionB(channel1) }
        launch { functionC(channel1) }
        launch { functionD(channel2, channel3) }
        launch { functionE(channel3, channel4) }
        launch { functionA(channel5) }
        launch { functionB(channel5) }
    }
}