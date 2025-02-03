/* 
{"deadlock":true,"nFunctions":2,"nCoroutines":2,"nChannels":3,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
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
package org.example.altered.test990
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(private val ch1: Channel<Int>, private val ch2: Channel<Int>) {
    suspend fun produce() {
        ch1.send(1)
        ch2.send(2)
    }
}

class Consumer(private val ch1: Channel<Int>, private val ch3: Channel<Int>) {
    suspend fun consume() {
        ch1.receive()
        ch3.send(3)
    }
}

fun main(): Unit= runBlocking {
    val ch1 = Channel<Int>()
    val ch2 = Channel<Int>()
    val ch3 = Channel<Int>()

    val producer = Producer(ch1, ch2)
    val consumer = Consumer(ch1, ch3)

    launch {
        producer.produce()
    }

    launch {
        consumer.consume()
    }

    ch2.receive()
    ch3.receive()
}

class RunChecker990: RunCheckerBase() {
    override fun block() = main()
}