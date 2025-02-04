/* 
{"deadlock":true,"nFunctions":1,"nCoroutines":3,"nChannels":4,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
- 4 different channels
- 3 different coroutines
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
package org.example.altered.test856
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(private val channel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            channel.send(i)
        }
    }
}

class Consumer(private val channel: Channel<Int>) {
    suspend fun consume(): Int {
        var total = 0
        for (i in 1..5) {
            total += channel.receive()
        }
        return total
    }
}

class MiddleMan(private val inputChannel: Channel<Int>, private val outputChannel: Channel<Int>) {
    suspend fun transfer() {
        for (i in 1..5) {
            val value = inputChannel.receive()
            outputChannel.send(value)
        }
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()

    val producer = Producer(channel1)
    val consumer = Consumer(channel4)
    val middleMan1 = MiddleMan(channel1, channel2)
    val middleMan2 = MiddleMan(channel3, channel4)

    launch {
        producer.produce()
    }

    launch {
        middleMan1.transfer()
    }

    launch {
        middleMan2.transfer()
    }

    val result = consumer.consume()
    println("Result: $result")
}

class RunChecker856: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}