/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":3,"nChannels":2,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 2 different channels
- 3 different coroutines
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
package org.example.altered.test652
import org.example.altered.test652.RunChecker652.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA(private val channelA: Channel<Int>, private val channelB: Channel<Int>) {
    suspend fun functionA() {
        val value = channelA.receive()
        channelB.send(value + 1)
    }
}

class ClassB(private val channelA: Channel<Int>, private val channelB: Channel<Int>) {
    suspend fun functionB() {
        val value = channelB.receive()
        channelA.send(value + 1)
    }
}

suspend fun functionC(channelA: Channel<Int>, channelB: Channel<Int>) {
    val value = channelA.receive()
    channelB.send(value + 1)
}

fun main(): Unit= runBlocking(pool) {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()

    val classA = ClassA(channelA, channelB)
    val classB = ClassB(channelA, channelB)

    launch(pool) {
        classA.functionA()
    }

    launch(pool) {
        classB.functionB()
    }

    launch(pool) {
        functionC(channelA, channelB)
    }

    channelA.send(1)
}

class RunChecker652: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}