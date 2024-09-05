/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":5,"nChannels":7,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 7 different channels
- 5 different coroutines
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
package org.example.generated.test285
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Sender1(val ch1: Channel<Int>, val ch2: Channel<Int>) {
    suspend fun send() {
        ch1.send(1)
        ch2.send(2)
    }
}

class Sender2(val ch3: Channel<Int>, val ch4: Channel<Int>) {
    suspend fun send() {
        ch3.send(3)
        ch4.send(4)
    }
}

fun receiveFromChannel(ch: Channel<Int>): Int = runBlocking {
    ch.receive()
}

suspend fun processChannels(ch5: Channel<Int>, ch6: Channel<Int>, ch7: Channel<Int>) = coroutineScope {
    val result1 = async { receiveFromChannel(ch5) }
    val result2 = async { receiveFromChannel(ch6) }
    val result3 = async { receiveFromChannel(ch7) }

    println("Result1: ${result1.await()}")
    println("Result2: ${result2.await()}")
    println("Result3: ${result3.await()}")
}

fun main(): Unit= runBlocking {
    val ch1 = Channel<Int>()
    val ch2 = Channel<Int>()
    val ch3 = Channel<Int>()
    val ch4 = Channel<Int>()
    val ch5 = Channel<Int>()
    val ch6 = Channel<Int>()
    val ch7 = Channel<Int>()

    val sender1 = Sender1(ch1, ch2)
    val sender2 = Sender2(ch3, ch4)

    launch {
        sender1.send()
    }

    launch {
        sender2.send()
    }

    launch {
        val received1 = receiveFromChannel(ch1)
        val received2 = receiveFromChannel(ch2)
        println("Received from Sender1: $received1, $received2")
        ch5.send(received1 + received2)
    }

    launch {
        val received3 = receiveFromChannel(ch3)
        val received4 = receiveFromChannel(ch4)
        println("Received from Sender2: $received3, $received4")
        ch6.send(received3 + received4)
    }

    launch {
        processChannels(ch5, ch6, ch7)
    }
}