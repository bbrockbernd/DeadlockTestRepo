/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":3,"nChannels":5,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 5 different channels
- 3 different coroutines
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
package org.example.altered.test772
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Sender(val ch1: Channel<Int>, val ch2: Channel<Int>) {
    suspend fun sendValues() {
        ch1.send(1)
        ch2.send(2)
    }
}

class Receiver(val ch3: Channel<Int>, val ch4: Channel<Int>, val ch5: Channel<Int>) {
    suspend fun receiveValues() {
        ch3.receive()
        ch4.receive()
        ch5.receive()
    }
}

suspend fun processChannelA(ch1: Channel<Int>, ch3: Channel<Int>) {
    ch3.send(ch1.receive())
}

suspend fun processChannelB(ch2: Channel<Int>, ch4: Channel<Int>) {
    ch4.send(ch2.receive())
}

fun main(): Unit= runBlocking {
    val ch1 = Channel<Int>()
    val ch2 = Channel<Int>()
    val ch3 = Channel<Int>()
    val ch4 = Channel<Int>()
    val ch5 = Channel<Int>()

    val sender = Sender(ch1, ch2)
    val receiver = Receiver(ch3, ch4, ch5)

    launch {
        sender.sendValues()
    }

    launch {
        processChannelA(ch1, ch3)
    }

    launch {
        processChannelB(ch2, ch4)
    }

    receiver.receiveValues() // This line should cause the deadlock
}

class RunChecker772: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}