/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":4,"nChannels":1,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 1 different channels
- 4 different coroutines
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
package org.example.altered.test870
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(val ch: Channel<Int>) {
    suspend fun produce() {
        ch.send(1)
    }
}

class Consumer(val ch: Channel<Int>) {
    suspend fun consume(): Int {
        return ch.receive()
    }
}

class Coordinator(val producer: Producer, val consumer: Consumer) {
    fun coordinate() = runBlocking {
        launch { producer.produce() }
        launch { consumer.consume() }
    }
}

fun deadlockPart1(channel: Channel<Int>) = runBlocking {
    coroutineScope {
        launch {
            channel.send(2)
        }

        launch {
            channel.receive()
        }

        launch {
            channel.send(3)
        }
    }
    channel.receive()
}

fun deadlockPart2(channel: Channel<Int>) = runBlocking {
    coroutineScope {
        launch {
            channel.receive()
        }

        launch {
            channel.send(4)
        }
    }
}

fun main(): Unit{
    val channel = Channel<Int>()

    val producer = Producer(channel)
    val consumer = Consumer(channel)
    val coordinator = Coordinator(producer, consumer)

    coordinator.coordinate()
    deadlockPart1(channel)
    deadlockPart2(channel)
}

class RunChecker870: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}