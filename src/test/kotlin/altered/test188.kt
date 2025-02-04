/* 
{"deadlock":true,"nFunctions":6,"nCoroutines":6,"nChannels":2,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 2 different channels
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
package org.example.altered.test188
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class A(val channelA: Channel<Int>, val channelB: Channel<String>)
class B(val channelA: Channel<Int>)
class C(val channelB: Channel<String>)
class D(val channelA: Channel<Int>, val channelB: Channel<String>)

suspend fun sendNumbers(channel: Channel<Int>) {
    channel.send(5)
    channel.receive()
}

suspend fun receiveNumbers(channel: Channel<Int>) {
    channel.send(channel.receive() + 1)
}

suspend fun sendStrings(channel: Channel<String>) {
    channel.send("Hello")
    channel.receive()
}

suspend fun receiveStrings(channel: Channel<String>) {
    channel.send(channel.receive() + " World")
}

suspend fun useChannels(channelA: Channel<Int>, channelB: Channel<String>) {
    sendNumbers(channelA)
    sendStrings(channelB)
}

suspend fun processChannels(channelA: Channel<Int>, channelB: Channel<String>) {
    receiveNumbers(channelA)
    receiveStrings(channelB)
}

fun main(): Unit= runBlocking {
    val channelA = Channel<Int>()
    val channelB = Channel<String>()

    val a = A(channelA, channelB)
    val b = B(channelA)
    val c = C(channelB)
    val d = D(channelA, channelB)

    launch { sendNumbers(a.channelA) }
    launch { receiveNumbers(b.channelA) }
    launch { sendStrings(c.channelB) }
    launch { receiveStrings(d.channelB) }
    launch { useChannels(a.channelA, a.channelB) }
    launch { processChannels(d.channelA, d.channelB) }
}

class RunChecker188: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}