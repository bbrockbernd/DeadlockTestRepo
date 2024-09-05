/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":6,"nChannels":6,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 6 different channels
- 6 different coroutines
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
package org.example.generated.test180
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class ProducerA(private val channel: SendChannel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            channel.send(i)
        }
    }
}

class ProducerB(private val channel: SendChannel<String>) {
    suspend fun produce() {
        val words = listOf("Kotlin", "Coroutines", "Channels", "Deadlock", "Detection")
        for (word in words) {
            channel.send(word)
        }
    }
}

class ConsumerA(private val channel: ReceiveChannel<Int>, private val processChannel: SendChannel<Int>) {
    suspend fun consume() {
        for (i in 1..5) {
            val value = channel.receive()
            processChannel.send(value * 2)
        }
    }
}

class ConsumerB(private val channel: ReceiveChannel<String>, private val processChannel: SendChannel<String>) {
    suspend fun consume() {
        for (i in 1..5) {
            val value = channel.receive()
            processChannel.send("$value Processed")
        }
    }
}

fun coroutineA(channel1: SendChannel<Int>, channel2: SendChannel<String>) = runBlocking {
    launch { ProducerA(channel1).produce() }
    launch { ProducerB(channel2).produce() }
}

fun coroutineB(channel1: ReceiveChannel<Int>, channel2: ReceiveChannel<String>, processChannel1: SendChannel<Int>, processChannel2: SendChannel<String>) = runBlocking {
    launch { ConsumerA(channel1, processChannel1).consume() }
    launch { ConsumerB(channel2, processChannel2).consume() }
}

fun main(): Unit = runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<String>()
    val processChannel1 = Channel<Int>(5)
    val processChannel2 = Channel<String>(5)

    launch { coroutineA(channel1, channel2) }
    launch { coroutineB(channel1, channel2, processChannel1, processChannel2) }

    launch {
        repeat(5) {
            println("Processed Int: ${processChannel1.receive()}")
            println("Processed String: ${processChannel2.receive()}")
        }
    }
}