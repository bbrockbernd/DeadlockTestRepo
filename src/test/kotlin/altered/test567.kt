/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":1,"nChannels":5,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 5 different channels
- 1 different coroutines
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
package org.example.altered.test567
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer1(private val channel1: Channel<Int>, private val channel2: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            channel1.send(i)
            channel2.send(i * 2)
        }
    }
}

class Producer2(private val channel3: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            channel3.send(i * 3)
        }
    }
}

class Consumer(private val channel1: Channel<Int>, private val channel2: Channel<Int>, private val channel3: Channel<Int>, private val channel4: Channel<Int>) {
    suspend fun consume() {
        repeat(5) {
            val value1 = channel1.receive()
            val value2 = channel2.receive()
            val value3 = channel3.receive()
            channel4.send(value1 + value2 + value3)
        }
    }
}

fun processResults(channel4: Channel<Int>, channel5: Channel<Int>) {
    runBlocking {
        repeat(5) {
            val result = channel4.receive()
            channel5.send(result * 2)
        }
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()

    val producer1 = Producer1(channel1, channel2)
    val producer2 = Producer2(channel3)
    val consumer = Consumer(channel1, channel2, channel3, channel4)

    launch { producer1.produce() }
    launch { producer2.produce() }
    launch { consumer.consume() }

    processResults(channel4, channel5)
}

class RunChecker567: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}