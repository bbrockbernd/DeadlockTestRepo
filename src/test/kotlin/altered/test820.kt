/* 
{"deadlock":false,"nFunctions":1,"nCoroutines":2,"nChannels":3,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
- 3 different channels
- 2 different coroutines
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
package org.example.altered.test820
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(val channelA: Channel<Int>, val channelB: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..10) {
            channelA.send(i)
            channelB.send(i * 2)
        }
    }
}

class Consumer(val channelA: Channel<Int>, val channelB: Channel<Int>, val channelC: Channel<Int>) {
    suspend fun consume() {
        repeat(10) {
            val a = channelA.receive()
            val b = channelB.receive()
            channelC.send(a + b)
        }
    }
}

fun main(): Unit= runBlocking {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()
    val channelC = Channel<Int>()

    launch {
        val producer = Producer(channelA, channelB)
        producer.produce()
    }

    launch {
        val consumer = Consumer(channelA, channelB, channelC)
        consumer.consume()
    }

    coroutineScope {
        repeat(10) {
            println(channelC.receive())
        }
    }
}

class RunChecker820: RunCheckerBase() {
    override fun block() = main()
}