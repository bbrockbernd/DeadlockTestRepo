/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":5,"nChannels":5,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 5 different channels
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
package org.example.altered.test691
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class DeadlockDetector {
    private val channelA = Channel<Int>(1)
    private val channelB = Channel<Int>(1)
    private val channelC = Channel<Int>(1)
    private val channelD = Channel<Int>(1)
    private val channelE = Channel<Int>(1)

    fun start() = runBlocking {
        launch { firstFunction() }
        launch { secondFunction() }
        launch { thirdFunction() }
        launch { fourthFunction() }
        launch { fifthFunction() }
    }

    private suspend fun firstFunction() {
        channelA.send(1)
        channelB.receive()
    }

    private suspend fun secondFunction() {
        channelB.send(2)
        channelC.receive()
    }

    private suspend fun thirdFunction() {
        channelC.send(3)
        channelD.receive()
    }

    private suspend fun fourthFunction() {
        channelD.send(4)
        channelE.receive()
    }

    private suspend fun fifthFunction() {
        channelE.send(5)
        channelA.receive()
    }
}

fun main(): Unit{
    val detector = DeadlockDetector()
    detector.start()
}

class RunChecker691: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}