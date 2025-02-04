/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":5,"nChannels":2,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 2 different channels
- 5 different coroutines
- 0 different classes

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
package org.example.altered.test611
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()

    launch { coroutine1(channel1, channel2) }
    launch { coroutine2(channel1, channel2) }
    launch { coroutine3(channel1) }
    launch { coroutine4(channel2) }
    launch { coroutine5(channel2) }
}

suspend fun coroutine1(channel1: Channel<Int>, channel2: Channel<Int>) {
    for (i in 1..5) {
        channel1.send(i)
        channel2.receive()
    }
}

suspend fun coroutine2(channel1: Channel<Int>, channel2: Channel<Int>) {
    for (i in 1..5) {
        channel2.send(i)
        coroutineScope { coroutine6(channel1) }
    }
}

suspend fun coroutine3(channel1: Channel<Int>) {
    for (i in 1..5) {
        val received = channel1.receive()
        println("Coroutine 3 received: $received")
    }
}

suspend fun coroutine4(channel2: Channel<Int>) {
    for (i in 1..5) {
        val received = channel2.receive()
        println("Coroutine 4 received: $received")
    }
}

suspend fun coroutine5(channel2: Channel<Int>) {
    for (i in 1..5) {
        channel2.send(i * 2)
    }
}

suspend fun coroutine6(channel1: Channel<Int>) {
    for (i in 1..5) {
        channel1.receive()
    }
}

class RunChecker611: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}