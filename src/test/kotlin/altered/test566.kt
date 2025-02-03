/* 
{"deadlock":true,"nFunctions":1,"nCoroutines":1,"nChannels":2,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
- 2 different channels
- 1 different coroutines
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
package org.example.altered.test566
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(private val channelA: Channel<Int>, private val channelB: Channel<Int>) {
    suspend fun produce() {
        channelA.send(1)
        channelB.send(2)
    }
}

class Consumer(private val channelA: Channel<Int>, private val channelB: Channel<Int>) {
    suspend fun consume() {
        val a = channelA.receive()
        val b = channelB.receive()
    }
}

fun main(): Unit= runBlocking {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()

    val producer = Producer(channelA, channelB)
    val consumer = Consumer(channelA, channelB)

    launch {
        producer.produce()
    }

    launch {
        consumer.consume()
    }
}

class RunChecker566: RunCheckerBase() {
    override fun block() = main()
}