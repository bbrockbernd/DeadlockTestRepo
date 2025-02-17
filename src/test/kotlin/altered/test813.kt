/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":4,"nChannels":1,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 1 different channels
- 4 different coroutines
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
package org.example.altered.test813
import org.example.altered.test813.RunChecker813.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class A {
    val channel = Channel<Int>()
    suspend fun sendValue(value: Int) {
        channel.send(value)
    }
}

class B {
    suspend fun receiveValue(channel: Channel<Int>): Int {
        return channel.receive()
    }
}

class C {
    suspend fun processValue(value: Int): Int {
        delay(100)
        return value * 2
    }
}

fun startCoroutines(a: A, b: B, c: C) {
    runBlocking(pool) {
        launch(pool) {
            // Coroutine 1: send a value
            a.sendValue(1)
        }
        launch(pool) {
            // Coroutine 2: receive and process the value
            val value = b.receiveValue(a.channel)
            val processedValue = c.processValue(value)
            a.sendValue(processedValue)
        }
        launch(pool) {
            // Coroutine 3: receive and print the processed value
            val result = b.receiveValue(a.channel)
            println("Result: $result")
        }
        launch(pool) {
            // Coroutine 4: send another value causing a deadlock
            val anotherValue = b.receiveValue(a.channel)
            a.sendValue(anotherValue)
        }
    }
}

fun main(): Unit{
    val a = A()
    val b = B()
    val c = C()
    startCoroutines(a, b, c)
}

class RunChecker813: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}