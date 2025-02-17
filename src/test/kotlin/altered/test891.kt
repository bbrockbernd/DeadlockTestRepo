/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":5,"nChannels":5,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 5 different channels
- 5 different coroutines
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
package org.example.altered.test891
import org.example.altered.test891.RunChecker891.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA {
    val channelA = Channel<Int>()
    suspend fun functionA() {
        repeat(5) {
            channelA.send(it)
        }
    }
}

class ClassB {
    val channelB = Channel<Int>()
    suspend fun functionB(channelA: Channel<Int>) {
        repeat(5) {
            val value = channelA.receive()
            channelB.send(value * 2)
        }
    }
}

class ClassC {
    val channelC = Channel<Int>()
    suspend fun functionC(channelB: Channel<Int>) {
        repeat(5) {
            val value = channelB.receive()
            channelC.send(value + 1)
        }
    }
}

suspend fun functionD(channelC: Channel<Int>, channelA: Channel<Int>) {
    repeat(5) {
        val value = channelC.receive()
        channelA.send(value - 1)
    }
}

fun main(): Unit= runBlocking(pool) {
    val classA = ClassA()
    val classB = ClassB()
    val classC = ClassC()

    val job1 = launch(pool) { classA.functionA() }
    val job2 = launch(pool) { classB.functionB(classA.channelA) }
    val job3 = launch(pool) { classC.functionC(classB.channelB) }
    val job4 = launch(pool) { functionD(classC.channelC, classA.channelA) }

    val channelD = Channel<Int>()
    val job5 = launch(pool) {
        suspend {
            channelD.receive()
        }.invoke()
    }

    job1.join()
    job2.join()
    job3.join()
    job4.join()
    job5.join()
}

class RunChecker891: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}