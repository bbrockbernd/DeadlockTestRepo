/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":3,"nChannels":2,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 2 different channels
- 3 different coroutines
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
package org.example.altered.test570
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class DeadlockDemo {
    private val channel1 = Channel<Int>()
    private val channel2 = Channel<Int>()

    fun start() = runBlocking {
        launch { coroutineA() }
        launch { coroutineB() }
        launch { coroutineC() }
    }

    suspend fun coroutineA() {
        val value = channel1.receive()
        println("Coroutine A received: $value")
        channel2.send(value * 2)
    }

    suspend fun coroutineB() {
        val value = channel2.receive()
        println("Coroutine B received: $value")
        channel1.send(value + 3)
    }

    suspend fun coroutineC() {
        channel1.send(1)
        val value = channel2.receive()
        println("Coroutine C received: $value")
    }
}

fun main(): Unit{
    val demo = DeadlockDemo()
    demo.start()
}

class RunChecker570: RunCheckerBase() {
    override fun block() = main()
}