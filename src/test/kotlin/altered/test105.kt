/* 
{"deadlock":false,"nFunctions":6,"nCoroutines":1,"nChannels":3,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 3 different channels
- 1 different coroutines
- 5 different classes

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
package org.example.altered.test105
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer1(val channel: Channel<Int>) {
    suspend fun produce() {
        repeat(5) {
            channel.send(it * 1)
        }
    }
}

class Producer2(val channel: Channel<Int>) {
    suspend fun produce() {
        repeat(5) {
            channel.send(it * 2)
        }
    }
}

class Producer3(val channel: Channel<Int>) {
    suspend fun produce() {
        repeat(5) {
            channel.send(it * 3)
        }
    }
}

class Consumer1(val channel: Channel<Int>) {
    suspend fun consume() {
        repeat(5) {
            println("Consumer1 received: ${channel.receive()}")
        }
    }
}

class Consumer2(val channel: Channel<Int>) {
    suspend fun consume() {
        repeat(5) {
            println("Consumer2 received: ${channel.receive()}")
        }
    }
}

class Consumer3(val channel: Channel<Int>) {
    suspend fun consume() {
        repeat(5) {
            println("Consumer3 received: ${channel.receive()}")
        }
    }
}

fun producer1Func(prod1: Producer1) = runBlocking {
    prod1.produce()
}

fun producer2Func(prod2: Producer2) = runBlocking {
    prod2.produce()
}

fun producer3Func(prod3: Producer3) = runBlocking {
    prod3.produce()
}

fun consumer1Func(cons1: Consumer1) = runBlocking {
    cons1.consume()
}

fun consumer2Func(cons2: Consumer2) = runBlocking {
    cons2.consume()
}

fun consumer3Func(cons3: Consumer3) = runBlocking {
    cons3.consume()
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()

    val producer1 = Producer1(channel1)
    val producer2 = Producer2(channel2)
    val producer3 = Producer3(channel3)

    val consumer1 = Consumer1(channel1)
    val consumer2 = Consumer2(channel2)
    val consumer3 = Consumer3(channel3)

    launch { producer1Func(producer1) }
    launch { producer2Func(producer2) }
    launch { producer3Func(producer3) }

    launch { consumer1Func(consumer1) }
    launch { consumer2Func(consumer2) }
    launch { consumer3Func(consumer3) }
}

class RunChecker105: RunCheckerBase() {
    override fun block() = main()
}