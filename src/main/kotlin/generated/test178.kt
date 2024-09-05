/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":6,"nChannels":6,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 6 different channels
- 6 different coroutines
- 4 different classes

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
package org.example.generated.test178
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class A(val channel1: Channel<Int>, val channel2: Channel<Int>) {
    suspend fun sendValues() {
        for (i in 1..5) {
            channel1.send(i)
            channel2.send(i * 2)
        }
    }
}

class B(val channel3: Channel<Int>, val channel4: Channel<Int>) {
    suspend fun receiveAndSend(channel1: Channel<Int>, channel2: Channel<Int>) {
        for (i in 1..5) {
            val value1 = channel1.receive()
            val value2 = channel2.receive()
            channel3.send(value1 + value2)
            channel4.send(value1 * value2)
        }
    }
}

class C(val channel5: Channel<Int>) {
    suspend fun calculateFinal(channel3: Channel<Int>, channel4: Channel<Int>) {
        for (i in 1..5) {
            val sum = channel3.receive()
            val product = channel4.receive()
            channel5.send(sum + product)
        }
    }
}

class D {
    suspend fun printResults(channel5: Channel<Int>) {
        for (i in 1..5) {
            val result = channel5.receive()
            println("Final result: $result")
        }
    }
}

fun main(): Unit = runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()

    val a = A(channel1, channel2)
    val b = B(channel3, channel4)
    val c = C(channel5)
    val d = D()

    launch { a.sendValues() }
    launch { b.receiveAndSend(channel1, channel2) }
    launch { c.calculateFinal(channel3, channel4) }
    launch { d.printResults(channel5) }
}