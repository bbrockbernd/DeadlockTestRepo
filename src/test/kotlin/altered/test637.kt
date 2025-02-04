/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":2,"nChannels":5,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 5 different channels
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
package org.example.altered.test637
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(private val ch1: Channel<Int>, private val ch2: Channel<Int>) {
    suspend fun produceValues() {
        ch1.send(1)
        ch2.send(2)
    }
}

class Consumer(private val ch1: Channel<Int>, private val ch2: Channel<Int>, private val ch3: Channel<Int>) {
    suspend fun consumeValues() {
        val value1 = ch1.receive()
        val value2 = ch2.receive()
        ch3.send(value1 + value2)
    }
}

fun functionA(producer: Producer) = runBlocking {
    producer.produceValues()
}

fun functionB(consumer: Consumer) = runBlocking {
    consumer.consumeValues()
}

fun main(): Unit= runBlocking {
    val ch1 = Channel<Int>()
    val ch2 = Channel<Int>()
    val ch3 = Channel<Int>()
    val ch4 = Channel<Int>()
    val ch5 = Channel<Int>()

    val producer = Producer(ch1, ch2)
    val consumer = Consumer(ch1, ch2, ch3)

    launch {
        functionA(producer)
    }

    launch {
        functionB(consumer)
    }

    ch4.send(ch3.receive()) // ch4 is supposed to receive a value from ch3, but it never happens due to dependency loop
    println(ch5.receive()) // Trying to print from ch5, which never actually gets a value
}

class RunChecker637: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}