/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":3,"nChannels":4,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 4 different channels
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
package org.example.altered.test160
import org.example.altered.test160.RunChecker160.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ComponentA {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()

    suspend fun functionA() {
        channelA.send(1)
        channelB.send(channelA.receive() * 2) // Deadlock here since functionB waits on channelA
    }
}

class ComponentB {
    val channelC = Channel<Int>()
    val channelD = Channel<Int>()

    suspend fun functionB() {
        channelC.send(channelD.receive() + 1) 
        channelD.send(channelC.receive() - 1) // Deadlock here since functionD waits on channelC
    }
}

fun functionC(channelC: Channel<Int>, channelD: Channel<Int>) = runBlocking(pool) {
    launch(pool) {
        channelD.send(10)
        val value = channelC.receive()
        println("Received in functionC: $value")
    }
}

fun functionD(channelA: Channel<Int>, channelC: Channel<Int>) = runBlocking(pool) {
    launch(pool) {
        val value = channelA.receive()
        channelC.send(value * 3) // Deadlock with functionB on channelC
    }
}

fun main(): Unit= runBlocking(pool) {
    val compA = ComponentA()
    val compB = ComponentB()

    launch(pool) { compA.functionA() }
    launch(pool) { compB.functionB() }
    functionC(compB.channelC, compB.channelD)
    functionD(compA.channelA, compB.channelC)
}

class RunChecker160: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}