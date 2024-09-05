/* 
{"deadlock":true,"nFunctions":2,"nCoroutines":3,"nChannels":6,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 6 different channels
- 3 different coroutines
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
package org.example.generated.test280
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class ChannelHolderA(val ch1: Channel<Int>, val ch2: Channel<Int>)
class ChannelHolderB(val ch3: Channel<Int>, val ch4: Channel<Int>)
class ChannelHolderC(val ch5: Channel<Int>, val ch6: Channel<Int>)

fun fillChannels(holderA: ChannelHolderA, holderB: ChannelHolderB) {
    runBlocking {
        launch {
            holderA.ch1.send(1)
            val received = holderA.ch2.receive()
            holderB.ch3.send(received)
        }
        launch {
            val received = holderB.ch4.receive()
            holderA.ch2.send(received)
        }
    }
}

fun exchangeChannels(holderC: ChannelHolderC, holderB: ChannelHolderB) {
    runBlocking {
        launch {
            val received = holderC.ch5.receive()
            holderB.ch4.send(received)
        }
        launch {
            holderC.ch6.send(2)
            holderB.ch3.receive()
        }
    }
}

fun main(): Unit{
    val ch1 = Channel<Int>()
    val ch2 = Channel<Int>()
    val ch3 = Channel<Int>()
    val ch4 = Channel<Int>()
    val ch5 = Channel<Int>()
    val ch6 = Channel<Int>()

    val holderA = ChannelHolderA(ch1, ch2)
    val holderB = ChannelHolderB(ch3, ch4)
    val holderC = ChannelHolderC(ch5, ch6)

    fillChannels(holderA, holderB)
    exchangeChannels(holderC, holderB)
}