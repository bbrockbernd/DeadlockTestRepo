/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":2,"nChannels":3,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
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
package org.example.altered.test880
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class Producer(val channel1: Channel<Int>, val channel2: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            channel1.send(i)
            channel2.send(i * 2)
        }
    }
}

class Consumer(val channel1: Channel<Int>, val channel2: Channel<Int>, val channel3: Channel<Int>) {
    suspend fun consume() {
        for (i in 1..5) {
            val received1 = channel1.receive()
            val received2 = channel2.receive()
            channel3.send(received1 + received2)
        }
    }
}

suspend fun report(channel3: Channel<Int>) {
    repeat(5) {
        println("Report: ${channel3.receive()}")
    }
}

fun setupChannels(): Triple<Channel<Int>, Channel<Int>, Channel<Int>> {
    val channel1 = Channel<Int>(Channel.UNLIMITED)
    val channel2 = Channel<Int>(Channel.UNLIMITED)
    val channel3 = Channel<Int>(Channel.UNLIMITED)
    return Triple(channel1, channel2, channel3)
}

fun main(): Unit= runBlocking {
    val (channel1, channel2, channel3) = setupChannels()
    val producer = Producer(channel1, channel2)
    val consumer = Consumer(channel1, channel2, channel3)

    launch { producer.produce() }
    launch { consumer.consume() }

    report(channel3)
}

class RunChecker880: RunCheckerBase() {
    override fun block() = main()
}