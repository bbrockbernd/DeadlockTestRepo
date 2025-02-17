/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":4,"nChannels":5,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 5 different channels
- 4 different coroutines
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
package org.example.altered.test927
import org.example.altered.test927.RunChecker927.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()

    fun funcA() = runBlocking(pool) {
        launch(pool) {
            val result = channelA.receive()
            println("Received in funcA: $result")
        }
    }

    fun funcB() = runBlocking(pool) {
        launch(pool) {
            channelB.send(1)
            println("Sent in funcB")
        }
    }
}

class ClassB {
    val channelC = Channel<Int>()
    val channelD = Channel<Int>()
    val channelE = Channel<Int>()

    fun funcC() = runBlocking(pool) {
        launch(pool) {
            val result = channelC.receive()
            println("Received in funcC: $result")
            channelD.send(result)
            println("Sent in funcC to channelD")
        }
    }

    fun funcD() = runBlocking(pool) {
        launch(pool) {
            val result = channelD.receive()
            println("Received in funcD: $result")
            channelE.send(result)
            println("Sent in funcD to channelE")
        }
    }
}

fun main(): Unit= runBlocking(pool) {
    val classA = ClassA()
    val classB = ClassB()

    val job1 = launch(pool) {
        classA.funcA()
    }

    val job2 = launch(pool) {
        classA.funcB()
    }

    val job3 = launch(pool) {
        classB.funcC()
    }

    val job4 = launch(pool) {
        classB.funcD()
    }

    classA.channelA.send(42)
    println("Sent from main to channelA")

    classB.channelC.send(43)
    println("Sent from main to channelC")

    job1.join()
    job2.join()
    job3.join()
    job4.join()
}

class RunChecker927: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}