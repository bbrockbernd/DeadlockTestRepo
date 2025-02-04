/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":5,"nChannels":4,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 4 different channels
- 5 different coroutines
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
package org.example.altered.test789
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class DeadlockExample {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()

    fun func1() = runBlocking {
        launch {
            channel1.send(1)
            channel2.receive()
        }
        launch {
            func2()
        }
    }

    suspend fun func2() = coroutineScope {
        launch {
            channel2.send(2)
            func3()
        }
    }

    suspend fun func3() = coroutineScope {
        launch {
            val value = channel3.receive()
            func4()
            func5()
        }
    }

    suspend fun func4() = coroutineScope {
        launch {
            channel3.send(3)
            channel4.receive()
        }
    }

    suspend fun func5() = coroutineScope {
        launch {
            channel4.send(4)
            channel1.receive()
        }
    }
}

fun main(): Unit{
    val example = DeadlockExample()
    example.func1()
}

class RunChecker789: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}