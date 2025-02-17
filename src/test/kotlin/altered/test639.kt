/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":5,"nChannels":4,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 4 different channels
- 5 different coroutines
- 0 different classes

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
package org.example.altered.test639
import org.example.altered.test639.RunChecker639.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

val channelA = Channel<Int>()
val channelB = Channel<Int>()
val channelC = Channel<Int>(1)
val channelD = Channel<Int>(1)

fun func1() {
    runBlocking(pool) {
        launch(pool) { 
            channelA.send(1)
            channelB.receive()
        }
    }
}

fun func2() {
    runBlocking(pool) {
        launch(pool) { 
            channelA.receive()
            channelC.send(2)
        }
    }
}

fun func3() {
    runBlocking(pool) {
        launch(pool) { 
            channelC.receive()
            channelD.send(3)
        }
    }
}

fun func4() {
    runBlocking(pool) {
        launch(pool) { 
            channelD.receive()
            channelB.send(4)
        }
    }
}

fun func5() {
    runBlocking(pool) {
        launch(pool) { 
            channelA.receive()
            channelD.send(5)
        }
    }
}

fun main(): Unit{
    runBlocking(pool) {
        launch(pool) { func1() }
        launch(pool) { func2() }
        launch(pool) { func3() }
        launch(pool) { func4() }
        launch(pool) { func5() }
    }
}

class RunChecker639: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}