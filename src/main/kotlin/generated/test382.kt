/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":5,"nChannels":6,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 6 different channels
- 5 different coroutines
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
package org.example.generated.test382
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class A(val input1: Channel<Int>, val output1: Channel<Int>) {
    suspend fun processA() {
        for (i in 1..5) {
            val value = input1.receive()
            output1.send(value + 1)
        }
    }
}

class B(val input2: Channel<Int>, val output2: Channel<Int>) {
    suspend fun processB() {
        for (i in 1..5) {
            val value = input2.receive()
            output2.send(value * 2)
        }
    }
}

class C(val input3: Channel<Int>, val output3: Channel<Int>) {
    suspend fun processC() {
        for (i in 1..5) {
            val value = input3.receive()
            output3.send(value - 1)
        }
    }
}

class Producer1(val outputChannel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            outputChannel.send(i)
        }
    }
}

class Consumer1(val inputChannel: Channel<Int>) {
    suspend fun consume() {
        for (i in 1..5) {
            val value = inputChannel.receive()
            println("Consumer1 received: $value")
        }
    }
}

fun setupChannels(): List<Channel<Int>> {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()
    val channel6 = Channel<Int>()
    return listOf(channel1, channel2, channel3, channel4, channel5, channel6)
}

fun main(): Unit= runBlocking {
    val (channel1, channel2, channel3, channel4, channel5, channel6) = setupChannels()

    val a = A(channel1, channel2)
    val b = B(channel2, channel3)
    val c = C(channel3, channel4)
    val producer1 = Producer1(channel1)
    val consumer1 = Consumer1(channel4)

    launch { a.processA() }
    launch { b.processB() }
    launch { c.processC() }
    launch { producer1.produce() }
    launch { consumer1.consume() }
}