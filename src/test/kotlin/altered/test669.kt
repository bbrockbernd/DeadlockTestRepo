/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":2,"nChannels":4,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 4 different channels
- 2 different coroutines
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
package org.example.altered.test669
import org.example.altered.test669.RunChecker669.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()

    suspend fun functionA() {
        for (i in 1..10) {
            channelA.send(i)
        }
    }

    suspend fun functionB() {
        for (i in 1..10) {
            println("ClassA received: ${channelB.receive()}")
        }
    }
}

class ClassB {
    val channelC = Channel<Int>()
    val channelD = Channel<Int>()

    suspend fun functionC() {
        for (i in 11..20) {
            channelC.send(i)
        }
    }

    suspend fun functionD() {
        for (i in 11..20) {
            println("ClassB received: ${channelD.receive()}")
        }
    }
}

class Connector {
    val classA = ClassA()
    val classB = ClassB()

    suspend fun connectChannels() {
        for (i in 1..10) {
            classA.channelB.send(classA.channelA.receive())
        }

        for (i in 11..20) {
            classB.channelD.send(classB.channelC.receive())
        }
    }
}

fun main(): Unit= runBlocking(pool) {
    val connector = Connector()

    launch(pool) {
        connector.classA.functionA()
    }

    launch(pool) {
        connector.classB.functionC()
    }

    coroutineScope {
        launch(pool) {
            connector.classA.functionB()
        }

        launch(pool) {
            connector.classB.functionD()
        }

        launch(pool) {
            connector.connectChannels()
        }
    }
}

class RunChecker669: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}