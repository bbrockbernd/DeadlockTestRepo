/* 
{"deadlock":true,"nFunctions":2,"nCoroutines":3,"nChannels":4,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 4 different channels
- 3 different coroutines
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
package org.example.altered.test519
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun main(): Unit= runBlocking {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()
    val channelC = Channel<Int>()
    val channelD = Channel<Int>()

    launch { coroutine1(channelA, channelB, channelC) }
    launch { coroutine2(channelB, channelC, channelD) }
    launch { coroutine3(channelD, channelA) }

    coroutineScope {
        // Ensuring that all coroutines run leading to a deadlock
    }
}

suspend fun coroutine1(channelA: Channel<Int>, channelB: Channel<Int>, channelC: Channel<Int>) {
    channelA.send(1)
    channelB.send(channelA.receive())
    channelC.receive()
}

suspend fun coroutine2(channelB: Channel<Int>, channelC: Channel<Int>, channelD: Channel<Int>) {
    channelB.receive()
    channelC.send(2)
    channelD.send(channelC.receive())
}

suspend fun coroutine3(channelD: Channel<Int>, channelA: Channel<Int>) {
    channelD.receive()
    channelA.receive()
}

class RunChecker519: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}