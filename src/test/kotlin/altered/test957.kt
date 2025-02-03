/* 
{"deadlock":false,"nFunctions":1,"nCoroutines":4,"nChannels":4,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
- 4 different channels
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
package org.example.altered.test957
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(val channel1: Channel<Int>, val channel2: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            delay(100)
            channel1.send(i)
            channel2.send(i * 10)
        }
        channel1.close()
        channel2.close()
    }
}

class Consumer(val channel3: Channel<Int>, val channel4: Channel<Int>) {
    suspend fun consume() {
        while (!channel3.isClosedForReceive || !channel4.isClosedForReceive) {
            val value1 = channel3.receiveCatching()
            val value2 = channel4.receiveCatching()
            if (value1.isSuccess) {
                println("Consumer received from channel3: ${value1.getOrNull()}")
            }
            if (value2.isSuccess) {
                println("Consumer received from channel4: ${value2.getOrNull()}")
            }
        }
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()

    val producer = Producer(channel1, channel2)
    val consumer = Consumer(channel3, channel4)

    launch {
        for (i in channel1) {
            channel3.send(i + 1)
        }
        channel3.close()
    }

    launch {
        for (i in channel2) {
            channel4.send(i + 1)
        }
        channel4.close()
    }

    launch {
        producer.produce()
    }

    launch {
        consumer.consume()
    }
}

class RunChecker957: RunCheckerBase() {
    override fun block() = main()
}