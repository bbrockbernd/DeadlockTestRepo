/* 
{"deadlock":true,"nFunctions":6,"nCoroutines":7,"nChannels":8,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 8 different channels
- 7 different coroutines
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
package org.example.altered.test220
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class ProcessorA {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()

    suspend fun processA1() {
        val value = channelA.receive()
        channelB.send(value + 1)
    }

    suspend fun processA2() {
        val value = channelB.receive()
        channelA.send(value + 1)
    }
}

class ProcessorB {
    val channelC = Channel<Int>()
    val channelD = Channel<Int>()

    suspend fun processB1() {
        val value = channelC.receive()
        channelD.send(value + 1)
    }

    suspend fun processB2() {
        val value = channelD.receive()
        channelC.send(value + 1)
    }
}

class ProcessorC {
    val channelE = Channel<Int>()
    val channelF = Channel<Int>()
    val channelG = Channel<Int>()
    val channelH = Channel<Int>()

    suspend fun processC1() {
        val value = channelE.receive()
        channelF.send(value + 1)
    }

    suspend fun processC2() {
        val value = channelF.receive()
        channelG.send(value + 1)
    }

    suspend fun processC3() {
        val value = channelG.receive()
        channelH.send(value + 1)
    }

    suspend fun processC4() {
        val value = channelH.receive()
        channelE.send(value + 1)
    }
}

fun main(): Unit= runBlocking {
    val processorA = ProcessorA()
    val processorB = ProcessorB()
    val processorC = ProcessorC()

    launch {
        processorA.processA1()
    }
    launch {
        processorA.processA2()
    }
    launch {
        processorB.processB1()
    }
    launch {
        processorB.processB2()
    }
    launch {
        processorC.processC1()
    }
    launch {
        processorC.processC2()
    }
    launch {
        processorC.processC3()
    }
    launch {
        processorC.processC4()
    }

    processorA.channelA.send(0)
    processorB.channelC.send(0)
    processorC.channelE.send(0)
}

class RunChecker220: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}