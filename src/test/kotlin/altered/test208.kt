/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":4,"nChannels":5,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 5 different channels
- 4 different coroutines
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
package org.example.altered.test208
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ChannelA(val channel: Channel<Int>)
class ChannelB(val channel: Channel<Int>)
class ChannelC(val channel: Channel<Int>)
class ChannelD(val channel: Channel<Int>)
class ChannelE(val channel: Channel<Int>)

fun firstFunction(chA: ChannelA, chB: ChannelB) {
    runBlocking {
        launch {
            val received = chA.channel.receive()
            chB.channel.send(received + 1)
        }
    }
}

fun secondFunction(chB: ChannelB, chC: ChannelC) {
    runBlocking {
        launch {
            val received = chB.channel.receive()
            chC.channel.send(received + 1)
        }
    }
}

fun thirdFunction(chC: ChannelC, chD: ChannelD) {
    runBlocking {
        launch {
            val received = chC.channel.receive()
            chD.channel.send(received + 1)
        }
    }
}

fun fourthFunction(chD: ChannelD, chA: ChannelA, chE: ChannelE) {
    runBlocking {
        launch {
            val received = chD.channel.receive()
            chA.channel.send(received + 1)
            chE.channel.send(received + 2)
        }
    }
}

fun main(): Unit{
    val chA = ChannelA(Channel<Int>(1))
    val chB = ChannelB(Channel<Int>(1))
    val chC = ChannelC(Channel<Int>(1))
    val chD = ChannelD(Channel<Int>(1))
    val chE = ChannelE(Channel<Int>(1))

    runBlocking {
        launch {
            firstFunction(chA, chB)
            secondFunction(chB, chC)
            thirdFunction(chC, chD)
            fourthFunction(chD, chA, chE)
        }

        launch {
            val value = chE.channel.receive()
            println("Final received value: $value")
        }

        chA.channel.send(1)
    }
}

class RunChecker208: RunCheckerBase() {
    override fun block() = main()
}