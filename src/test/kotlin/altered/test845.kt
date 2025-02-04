/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":1,"nChannels":5,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 5 different channels
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
package org.example.altered.test845
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Sender1(val ch1: Channel<Int>, val ch2: Channel<Int>) {
    suspend fun sendData() {
        ch1.send(1)
        ch2.send(2)
    }
}

class Sender2(val ch3: Channel<Int>, val ch4: Channel<Int>) {
    suspend fun sendData() {
        ch3.send(3)
        ch4.send(4)
    }
}

class Receiver(val ch1: Channel<Int>, val ch2: Channel<Int>, val ch3: Channel<Int>, val ch4: Channel<Int>, val ch5: Channel<Int>) {
    suspend fun receiveData() {
        val a = ch1.receive()
        val b = ch2.receive()
        val c = ch3.receive()
        val d = ch4.receive()
        ch5.send(a + b + c + d)
    }
}

suspend fun initiateSenders(sender1: Sender1, sender2: Sender2) {
    sender1.sendData()
    sender2.sendData()
}

fun main(): Unit= runBlocking {
    val ch1 = Channel<Int>()
    val ch2 = Channel<Int>()
    val ch3 = Channel<Int>()
    val ch4 = Channel<Int>()
    val ch5 = Channel<Int>()

    val sender1 = Sender1(ch1, ch2)
    val sender2 = Sender2(ch3, ch4)
    val receiver = Receiver(ch1, ch2, ch3, ch4, ch5)

    launch {
        initiateSenders(sender1, sender2)
    }

    launch {
        receiver.receiveData()
        println("Sum from channels: ${ch5.receive()}")
    }
}

class RunChecker845: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}