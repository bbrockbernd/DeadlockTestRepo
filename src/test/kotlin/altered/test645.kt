/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":4,"nChannels":3,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 3 different channels
- 4 different coroutines
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
package org.example.altered.test645
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class DeadlockTest {
    private val channel1 = Channel<Int>()
    private val channel2 = Channel<Int>()
    private val channel3 = Channel<Int>()

    fun startTest() = runBlocking {
        val job1 = launch { coroutine1() }
        val job2 = launch { coroutine2() }
        val job3 = launch { coroutine3() }
        val job4 = launch { coroutine4() }
    }

    private suspend fun coroutine1() {
        func1()
    }
    
    private suspend fun coroutine2() {
        func2()
    }

    private suspend fun coroutine3() {
        func3()
    }

    private suspend fun coroutine4() {
        func4()
    }

    private suspend fun func1() {
        channel1.send(1)
        channel2.receive()
    }

    private suspend fun func2() {
        channel2.send(2)
        channel3.receive()
    }

    private suspend fun func3() {
        channel3.send(3)
        channel1.receive()
    }

    private suspend fun func4() {
        // This function is used to enforce deadlock scenario.
        // Without this function in one of the coroutines, the deadlock might not happen.
        // Additional potential side interactions between coroutines leads to a clear deadlock when executing the above channels.
    }
}

fun main(): Unit{
    DeadlockTest().startTest()
}

class RunChecker645: RunCheckerBase() {
    override fun block() = main()
}