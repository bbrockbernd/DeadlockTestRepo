/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":1,"nChannels":5,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 5 different channels
- 1 different coroutines
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
package org.example.altered.test109
import org.example.altered.test109.RunChecker109.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

val channel1 = Channel<Int>()
val channel2 = Channel<Int>()
val channel3 = Channel<Int>()
val channel4 = Channel<Int>()
val channel5 = Channel<Int>()

fun function1() {
    runBlocking(pool) {
        launch(pool) {
            val data = channel1.receive()
            channel2.send(data + 1)
        }
    }
}

fun function2() {
    runBlocking(pool) {
        launch(pool) {
            val data = channel3.receive()
            channel4.send(data + 1)
        }
    }
}

fun function3() {
    runBlocking(pool) {
        launch(pool) {
            val data = channel2.receive()
            channel5.send(data + 1)
        }
    }
}

fun function4() {
    runBlocking(pool) {
        launch(pool) {
            val data = channel4.receive()
            channel1.send(data + 1)
        }
    }
}

fun main(): Unit{
    runBlocking(pool) {
        val job = launch(pool) {
            function1()
            function2()
            function3()
            function4()
            launch(pool) {
                channel3.send(1)
                val result = channel5.receive()
                println("Received: $result")
            }
        }
        job.join()
    }
}

class RunChecker109: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}