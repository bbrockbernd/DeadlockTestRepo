/* 
{"deadlock":false,"nFunctions":1,"nCoroutines":1,"nChannels":3,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
- 3 different channels
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
package org.example.altered.test847
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

class Consumer1(private val channel1: Channel<Int>, private val channel2: Channel<Int>) {
    suspend fun consume() {
        repeat(5) {
            val value = channel1.receive()
            channel2.send(value * 2)
        }
    }
}

class Consumer2(private val channel: Channel<Int>) {
    suspend fun consume() {
        repeat(5) {
            val value = channel.receive()
            println("Consumer2 received: $value")
        }
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()

    val producer = Producer(channel1)
    val consumer1 = Consumer1(channel1, channel2)
    val consumer2 = Consumer2(channel2)

    val job = launch {
        producer.produce()
        consumer1.consume()
        consumer2.consume()
    }

    job.join()
}

class RunChecker847: RunCheckerBase() {
    override fun block() = main()
}