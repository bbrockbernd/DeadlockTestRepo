/* 
{"deadlock":true,"nFunctions":6,"nCoroutines":6,"nChannels":4,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 4 different channels
- 6 different coroutines
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
package org.example.altered.test443
import org.example.altered.test443.RunChecker443.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ProcessorA {
    val channelA = Channel<Int>()
}

class ProcessorB {
    val channelB = Channel<Int>()
}

class ProcessorC {
    val channelC = Channel<Int>(1)
}

class ProcessorD {
    val channelD = Channel<Int>(1)
}

fun processAtoB(processorA: ProcessorA, processorB: ProcessorB) = runBlocking(pool) {
    processorA.channelA.send(1)
    processorB.channelB.receive()
}

fun processBtoC(processorB: ProcessorB, processorC: ProcessorC) = runBlocking(pool) {
    processorB.channelB.send(2)
    processorC.channelC.receive()
}

fun processCtoD(processorC: ProcessorC, processorD: ProcessorD) = runBlocking(pool) {
    processorC.channelC.send(3)
    processorD.channelD.receive()
}

fun processDtoA(processorD: ProcessorD, processorA: ProcessorA) = runBlocking(pool) {
    processorD.channelD.send(4)
    processorA.channelA.receive()
}

fun coroutineExample1(processorA: ProcessorA, processorB: ProcessorB) = runBlocking(pool) {
    launch(pool) {
        processAtoB(processorA, processorB)
    }
}

fun coroutineExample2(processorB: ProcessorB, processorC: ProcessorC, processorD: ProcessorD, processorA: ProcessorA) = runBlocking(pool) {
    launch(pool) {
        processBtoC(processorB, processorC)
        processDtoA(processorD, processorA)
    }
}

fun main(): Unit= runBlocking(pool) {
    val processorA = ProcessorA()
    val processorB = ProcessorB()
    val processorC = ProcessorC()
    val processorD = ProcessorD()

    launch(pool) { coroutineExample1(processorA, processorB) }
    launch(pool) { processCtoD(processorC, processorD) }
    launch(pool) { processAtoB(processorA, processorB) }
    launch(pool) { coroutineExample2(processorB, processorC, processorD, processorA) }
    launch(pool) { processBtoC(processorB, processorC) }
    launch(pool) { processDtoA(processorD, processorA) }
}

class RunChecker443: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}