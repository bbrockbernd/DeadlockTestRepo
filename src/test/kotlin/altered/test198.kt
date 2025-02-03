/* 
{"deadlock":true,"nFunctions":8,"nCoroutines":4,"nChannels":7,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
- 7 different channels
- 4 different coroutines
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
package org.example.altered.test198
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Processor {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()
    val channelC = Channel<Int>()
    val channelD = Channel<Int>()
    val channelE = Channel<Int>()
    val channelF = Channel<Int>()
    val channelG = Channel<Int>()

    suspend fun processA() {
        val value = channelA.receive()
        channelB.send(value)
    }

    suspend fun processB() {
        val value = channelB.receive()
        channelC.send(value)
    }

    suspend fun processC() {
        val value = channelC.receive()
        channelD.send(value)
    }

    suspend fun processD() {
        val value = channelD.receive()
        channelE.send(value)
    }

    suspend fun processE() {
        val value = channelE.receive()
        channelF.send(value)
    }

    suspend fun processF() {
        val value = channelF.receive()
        channelG.send(value)
    }

    suspend fun processG() {
        val value = channelG.receive()
        channelA.send(value)  // This creates a circular dependency and potential deadlock
    }

    fun initiateProcess() {
        runBlocking {
            launch { processA() }
            launch { processB() }
            launch { processC() }
            launch { processD() }
            launch { processE() }
            launch { processF() }
            launch { processG() }

            channelA.send(1)
        }
    }
}

fun main(): Unit{
    val processor = Processor()
    processor.initiateProcess()
}

class RunChecker198: RunCheckerBase() {
    override fun block() = main()
}