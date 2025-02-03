/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":1,"nChannels":2,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 2 different channels
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
package org.example.altered.test380
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class Producer1(val channel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            channel.send(i)
        }
        channel.close()
    }
}

class Producer2(val channel: Channel<Int>) {
    suspend fun produce() {
        for (i in 6..10) {
            channel.send(i)
        }
        channel.close()
    }
}

class Consumer(val channel1: Channel<Int>, val channel2: Channel<Int>) {
    suspend fun consume(): List<Int> {
        val result = mutableListOf<Int>()
        while (!channel1.isClosedForReceive && !channel2.isClosedForReceive) {
            val value1 = selectOrNull { 
                channel1.onReceive { it } 
            }
            if (value1 != null) result.add(value1)

            val value2 = selectOrNull { 
                channel2.onReceive { it } 
            }
            if (value2 != null) result.add(value2)
        }
        return result
    }
}

fun initializeChannel1(): Channel<Int> {
    return Channel()
}

fun initializeChannel2(): Channel<Int> {
    return Channel()
}

fun main(): Unit= runBlocking {
    val channel1 = initializeChannel1()
    val channel2 = initializeChannel2()

    val producer1 = Producer1(channel1)
    val producer2 = Producer2(channel2)
    val consumer = Consumer(channel1, channel2)

    launch { producer1.produce() }
    launch { producer2.produce() }

    println(consumer.consume())
}

class RunChecker380: RunCheckerBase() {
    override fun block() = main()
}