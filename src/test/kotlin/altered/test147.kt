/* 
{"deadlock":true,"nFunctions":7,"nCoroutines":6,"nChannels":4,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 4 different channels
- 6 different coroutines
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
package org.example.altered.test147
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class A(val channel1: Channel<Int>, val channel2: Channel<Int>) {
    suspend fun sendToChannel1(value: Int) {
        channel1.send(value)
    }

    suspend fun sendToChannel2(value: Int) {
        channel2.send(value)
    }
}

class B(val channel2: Channel<Int>, val channel3: Channel<Int>) {
    suspend fun receiveFromChannel2() {
        val item = channel2.receive()
        channel3.send(item)
    }
}

class C(val channel3: Channel<Int>, val channel4: Channel<Int>) {
    suspend fun receiveFromChannel3() {
        val item = channel3.receive()
        channel4.send(item)
    }
}

class D(val channel4: Channel<Int>) {
    suspend fun receiveFromChannel4() {
        channel4.receive()
    }
}

class E(val a: A, val b: B, val c: C, val d: D)

fun createChannels(): List<Channel<Int>> {
    val channels = List(4) { Channel<Int>() }
    return channels
}

fun createInstances(channels: List<Channel<Int>>): E {
    val a = A(channels[0], channels[1])
    val b = B(channels[1], channels[2])
    val c = C(channels[2], channels[3])
    val d = D(channels[3])
    return E(a, b, c, d)
}

fun main(): Unit= runBlocking {
    val channels = createChannels()
    val instances = createInstances(channels)

    launch { instances.a.sendToChannel1(1) }
    launch { instances.b.receiveFromChannel2() }
    launch { instances.c.receiveFromChannel3() }
    launch { instances.d.receiveFromChannel4() }
    launch { instances.a.sendToChannel2(2) }
    launch { instances.a.sendToChannel1(3) }  // This will cause a deadlock as the previous sendToChannel1 has not been received

    delay(1000L)  // Just to keep the main coroutine alive for a while to observe the deadlock
}

class RunChecker147: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}