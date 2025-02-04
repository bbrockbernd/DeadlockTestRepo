/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":4,"nChannels":5,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 5 different channels
- 4 different coroutines
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
package org.example.altered.test676
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(private val channel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            channel.send(i)
        }
        channel.close()
    }
}

class Consumer(private val channel: Channel<Int>) {
    suspend fun consume(): Int {
        var sum = 0
        repeat(5) {
            sum += channel.receive()
        }
        return sum
    }
}

suspend fun producerFunction(channel1: Channel<Int>, channel2: Channel<Int>) {
    val producer1 = Producer(channel1)
    val producer2 = Producer(channel2)
    coroutineScope {
        launch { producer1.produce() }
        launch { producer2.produce() }
    }
}

suspend fun consumerFunction(channel3: Channel<Int>, channel4: Channel<Int>, channel5: Channel<Int>) {
    val consumer1 = Consumer(channel3)
    val consumer2 = Consumer(channel4)
    val consumer3 = Consumer(channel5)
    coroutineScope {
        launch { consumer1.consume() }
        launch { consumer2.consume() }
        launch { consumer3.consume() }
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()

    launch {
        producerFunction(channel1, channel2)
    }

    launch {
        for (i in 1..5) {
            val value = channel1.receive() + channel2.receive()
            channel3.send(value)
            channel4.send(value)
            channel5.send(value)
        }
        channel3.close()
        channel4.close()
        channel5.close()
    }

    launch {
        consumerFunction(channel3, channel4, channel5)
    }
}

class RunChecker676: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}