/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":2,"nChannels":8,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 8 different channels
- 2 different coroutines
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
package org.example.altered.test29
import org.example.altered.test29.RunChecker29.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class SampleClass {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()
}

fun functionOne(sc: SampleClass) {
    runBlocking(pool) {
        launch(pool) {
            sc.channelA.send(1)
            sc.channelB.receive()
        }
    }
}

fun functionTwo(sc: SampleClass) {
    runBlocking(pool) {
        launch(pool) {
            sc.channelB.send(2)
            sc.channelA.receive()
        }
    }
}

val channelC = Channel<Int>()
val channelD = Channel<Int>()

fun functionThree() {
    runBlocking(pool) {
        launch(pool) {
            channelC.send(3)
            channelD.receive()
        }
    }
}

val channelE = Channel<Int>()
val channelF = Channel<Int>()
val channelG = Channel<Int>()
val channelH = Channel<Int>()

fun functionFour() {
    runBlocking(pool) {
        launch(pool) {
            channelE.send(5)
            channelF.receive()
            channelG.send(7)
            channelH.receive()
        }
    }
}

fun main(): Unit{
    val sc = SampleClass()

    functionOne(sc)
    functionTwo(sc)
    functionThree()
    functionFour()
}

class RunChecker29: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}