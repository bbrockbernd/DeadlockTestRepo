/* 
{"deadlock":true,"nFunctions":8,"nCoroutines":1,"nChannels":6,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
- 6 different channels
- 1 different coroutines
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
package org.example.altered.test47
import org.example.altered.test47.RunChecker47.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class DeadlockDemo {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()
    val channel6 = Channel<Int>()

    fun function1() {
        runBlocking(pool) {
            launch(pool) {
                channel1.send(1)
                val receivedValue = channel6.receive()
            }
        }
    }

    fun function2() {
        runBlocking(pool) {
            launch(pool) {
                val receivedValue = channel1.receive()
                channel2.send(receivedValue + 1)
            }
        }
    }

    fun function3() {
        runBlocking(pool) {
            launch(pool) {
                val receivedValue = channel2.receive()
                channel3.send(receivedValue + 1)
            }
        }
    }

    fun function4() {
        runBlocking(pool) {
            launch(pool) {
                val receivedValue = channel3.receive()
                channel4.send(receivedValue + 1)
            }
        }
    }

    fun function5() {
        runBlocking(pool) {
            launch(pool) {
                val receivedValue = channel4.receive()
                channel5.send(receivedValue + 1)
            }
        }
    }

    fun function6() {
        runBlocking(pool) {
            launch(pool) {
                val receivedValue = channel5.receive()
                channel6.send(receivedValue + 1)
            }
        }
    }

    fun function7() {
        runBlocking(pool) {
            launch(pool) {
                channel6.send(1)
                val receivedValue = channel1.receive()
            }
        }
    }

    fun function8() {
        runBlocking(pool) {
            launch(pool) {
                val receivedValue = channel6.receive()
                channel1.send(receivedValue + 1)
            }
        }
    }
}

fun main(): Unit {
    val demo = DeadlockDemo()
    demo.function1()
    demo.function2()
    demo.function3()
    demo.function4()
    demo.function5()
    demo.function6()
    demo.function7()
    demo.function8()
}

class RunChecker47: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}