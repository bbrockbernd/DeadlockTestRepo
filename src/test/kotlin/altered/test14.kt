/* 
{"deadlock":false,"nFunctions":6,"nCoroutines":5,"nChannels":8,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 8 different channels
- 5 different coroutines
- 1 different classes

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
package org.example.altered.test14
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ChannelHandler {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()
    val channelC = Channel<Int>()
    val channelD = Channel<Int>()
    val channelE = Channel<Int>(1)
    val channelF = Channel<Int>(1)
    val channelG = Channel<Int>()
    val channelH = Channel<Int>()
}

fun functionOne(channelA: Channel<Int>, channelB: Channel<Int>) = runBlocking {
    repeat(5) {
        channelA.send(1)
        channelB.receive()
    }
}

fun functionTwo(channelB: Channel<Int>, channelC: Channel<Int>, channelD: Channel<Int>) = runBlocking {
    repeat(5) {
        channelB.send(1)
        val received = channelC.receive()
        channelD.send(received)
    }
}

fun functionThree(channelC: Channel<Int>, channelF: Channel<Int>) = runBlocking {
    repeat(5) {
        val value = channelC.receive()
        channelF.send(value)
    }
}

fun functionFour(channelD: Channel<Int>, channelE: Channel<Int>) = runBlocking {
    repeat(5) {
        val received = channelD.receive()
        channelE.send(received)
    }
}

fun functionFive(channelE: Channel<Int>, channelG: Channel<Int>) = runBlocking {
    repeat(5) {
        val received = channelE.receive()
        channelG.send(received)
    }
}

fun functionSix(channelG: Channel<Int>, channelH: Channel<Int>) = runBlocking {
    repeat(5) {
        val received = channelG.receive()
        channelH.send(received)
    }
}

fun main(): Unit = runBlocking {
    val handler = ChannelHandler()

    launch { functionOne(handler.channelA, handler.channelB) }
    launch { functionTwo(handler.channelB, handler.channelC, handler.channelD) }
    launch { functionThree(handler.channelC, handler.channelF) }
    launch { functionFour(handler.channelD, handler.channelE) }
    launch { functionFive(handler.channelE, handler.channelG) }
    launch { functionSix(handler.channelG, handler.channelH) }

    coroutineScope {
        repeat(5) {
            handler.channelC.send(5)
        }

        repeat(5) {
            println(handler.channelH.receive())
        }
    }
}

class RunChecker14: RunCheckerBase() {
    override fun block() = main()
}