/* 
{"deadlock":true,"nFunctions":8,"nCoroutines":4,"nChannels":7,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
- 7 different channels
- 4 different coroutines
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
package org.example.generated.test357
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ChannelA(val ch: Channel<Int>)
class ChannelB(val ch: Channel<Int>)
class ChannelC(val ch: Channel<Int>)
class ChannelD(val ch: Channel<Int>)

val ch1 = Channel<Int>()
val ch2 = Channel<Int>()
val ch3 = Channel<Int>()
val ch4 = Channel<Int>()
val ch5 = Channel<Int>()
val ch6 = Channel<Int>()
val ch7 = Channel<Int>()

fun func1(chA: ChannelA) {
    runBlocking {
        launch {
            chA.ch.send(1)
            ch4.send(2)
        }
    }
}

fun func2(chB: ChannelB) {
    runBlocking {
        launch {
            chB.ch.receive()
            ch3.send(3)
        }
    }
}

fun func3(chC: ChannelC) {
    runBlocking {
        launch {
            chC.ch.send(4)
            ch6.send(5)
        }
    }
}

fun func4(chD: ChannelD) {
    runBlocking {
        launch {
            chD.ch.receive()
            ch5.send(6)
        }
    }
}

fun func5() {
    runBlocking {
        launch {
            ch1.receive()
            ch2.send(7)
        }
    }
}

fun func6() {
    runBlocking {
        launch {
            ch5.receive()
            ch1.send(8)
        }
    }
}

fun func7() {
    runBlocking {
        launch {
            ch6.receive()
            ch7.send(9)
        }
    }
}

fun func8() {
    runBlocking {
        launch {
            ch2.receive()
            ch3.receive()
        }
    }
}

fun main(): Unit{
    val channelA = ChannelA(ch1)
    val channelB = ChannelB(ch2)
    val channelC = ChannelC(ch3)
    val channelD = ChannelD(ch5)

    func1(channelA)
    func2(channelB)
    func3(channelC)
    func4(channelD)
    func5()
    func6()
    func7()
    func8()
}