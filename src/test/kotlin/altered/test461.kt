/* 
{"deadlock":true,"nFunctions":7,"nCoroutines":6,"nChannels":4,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 4 different channels
- 6 different coroutines
- 2 different classes

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
package org.example.altered.test461
import org.example.altered.test461.RunChecker461.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA(val channel1: Channel<Int>, val channel2: Channel<Int>) {
    suspend fun func1() {
        for (i in 1..5) {
            channel1.send(i)
        }
    }

    suspend fun func2() {
        for (i in 1..5) {
            channel2.send(i)
        }
    }

    suspend fun func3() {
        for (i in 1..5) {
            val received = channel1.receive()
            channel2.send(received)
        }
    }
}

class ClassB(val channel3: Channel<Int>, val channel4: Channel<Int>) {
    suspend fun func4() {
        for (i in 1..5) {
            channel3.send(i)
        }
    }

    suspend fun func5() {
        for (i in 1..5) {
            val received = channel3.receive()
            channel4.send(received)
        }
    }

    suspend fun func6() {
        for (i in 1..5) {
            val received = channel4.receive()
            channel3.send(received)
        }
    }
}

suspend fun func7(channel2: Channel<Int>, channel4: Channel<Int>) {
    for (i in 1..5) {
        val received = channel2.receive()
        channel4.send(received)
    }
}

fun main(): Unit= runBlocking(pool) {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()

    val classA = ClassA(channel1, channel2)
    val classB = ClassB(channel3, channel4)

    launch(pool) { classA.func1() }
    launch(pool) { classA.func2() }
    launch(pool) { classA.func3() }
    launch(pool) { classB.func4() }
    launch(pool) { classB.func5() }
    launch(pool) { classB.func6() }
    launch(pool) { func7(channel2, channel4) }
}

class RunChecker461: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}