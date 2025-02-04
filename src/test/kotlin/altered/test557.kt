/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":3,"nChannels":5,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 5 different channels
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
package org.example.altered.test557
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class DeadlockDetector {

    private val channelA = Channel<Int>()
    private val channelB = Channel<Int>()
    private val channelC = Channel<Int>()
    private val channelD = Channel<Int>()
    private val channelE = Channel<Int>()

    fun function1() = runBlocking {
        launch {
            receiveAthenSendB()
        }
        launch {
            receiveBthenSendC()
        }
        launch {
            competeForChannels() 
        }
    }

    fun function2() = runBlocking {
        launch {
            sendA()
        }
        launch {
            receiveC()
        }
    }

    private suspend fun sendA() {
        channelA.send(1)
    }

    private suspend fun receiveAthenSendB() {
        val receivedA = channelA.receive()
        channelB.send(receivedA)
    }

    private suspend fun receiveBthenSendC() {
        val receivedB = channelB.receive()
        channelC.send(receivedB)
    }

    private suspend fun competeForChannels() {
        coroutineScope {
            launch {
                channelD.receive() 
            }
            launch {
                channelE.send(2)
            }
        }
    }

    private suspend fun receiveC() {
        channelC.receive()
    }

}

fun main(): Unit{
    val detector = DeadlockDetector()
    detector.function1()
    detector.function2()
}

class RunChecker557: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}