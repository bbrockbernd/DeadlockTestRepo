/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":3,"nChannels":8,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 8 different channels
- 3 different coroutines
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
package org.example.altered.test206
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class ChannelTest {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()
    val channelC = Channel<Int>()
    val channelD = Channel<Int>()
    val channelE = Channel<Int>()
    val channelF = Channel<Int>()
    val channelG = Channel<Int>()
    val channelH = Channel<Int>()

    suspend fun functionOne() {
        channelA.send(1)
        channelB.send(channelA.receive())
        channelC.send(channelB.receive())
    }

    suspend fun functionTwo() {
        channelD.send(2)
        channelE.send(channelD.receive())
        channelF.send(channelE.receive())
    }

    suspend fun functionThree() {
        channelG.send(3)
        channelH.send(channelG.receive())
        channelA.send(channelH.receive())
    }

    suspend fun functionFour() {
        channelC.send(channelF.receive())
        channelB.send(channelC.receive())
        channelH.send(channelB.receive())
    }

    fun runTest() = runBlocking {
        launch {
            functionOne()
        }
        launch {
            functionTwo()
        }
        launch {
            functionThree()
        }
        functionFour()
    }
}

fun main(): Unit{
    val test = ChannelTest()
    test.runTest()
}

class RunChecker206: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}