/* 
{"deadlock":true,"nFunctions":2,"nCoroutines":1,"nChannels":3,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 3 different channels
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
package org.example.altered.test841
import org.example.altered.test841.RunChecker841.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class SampleClass {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()
    val channelC = Channel<Int>()

    suspend fun function1() {
        val receiveFromA = channelA.receive()
        channelB.send(receiveFromA)
        val receiveFromC = channelC.receive()
        println("Final result: $receiveFromC")
    }

    suspend fun function2() {
        val receiveFromB = channelB.receive()
        channelC.send(receiveFromB)
        val receiveFromA = channelA.receive()
        println("Additional result: $receiveFromA")
    }
}

fun main(): Unit= runBlocking(pool) {
    val sampleClass = SampleClass()

    launch(pool) {
        sampleClass.function1()
    }

    launch(pool) {
        sampleClass.function2()
    }

    sampleClass.channelA.send(42)
    sampleClass.channelA.send(21)
}

class RunChecker841: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}