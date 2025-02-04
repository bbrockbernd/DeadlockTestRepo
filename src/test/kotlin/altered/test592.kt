/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":4,"nChannels":5,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 5 different channels
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
package org.example.altered.test592
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer1(val channel: Channel<Int>) {
    suspend fun produce() {
        repeat(5) {
            channel.send(it)
        }
    }
}

class Producer2(val channel: Channel<Int>) {
    suspend fun produce() {
        repeat(5) {
            channel.send(it + 5)
        }
    }
}

class Consumer(val channel1: Channel<Int>, val channel2: Channel<Int>) {
    suspend fun consume() {
        repeat(5) {
            val value1 = channel1.receive()
            val value2 = channel2.receive()
            println("Consumed $value1 and $value2")
        }
    }
}

fun channel1Producer(channel: Channel<Int>): Producer1 {
    return Producer1(channel)
}

fun channel2Producer(channel: Channel<Int>): Producer2 {
    return Producer2(channel)
}

fun consumer(channel1: Channel<Int>, channel2: Channel<Int>): Consumer {
    return Consumer(channel1, channel2)
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()

    launch { channel1Producer(channel1).produce() }
    launch { channel2Producer(channel2).produce() }
    launch { consumer(channel1, channel2).consume() }

    launch {
        repeat(5) {
            channel3.send(it + 10)
            val received = channel4.receive()
            println("Extra consumer received $received")
        }
    }

    launch {
        repeat(5) {
            val received = channel3.receive()
            channel4.send(received + 10)
        }
    }
}

class RunChecker592: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}