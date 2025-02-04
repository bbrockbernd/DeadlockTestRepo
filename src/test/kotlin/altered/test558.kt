/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":3,"nChannels":4,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 4 different channels
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
package org.example.altered.test558
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.sendBlocking

class DeadlockDetector {
    private val channel1 = Channel<Int>()
    private val channel2 = Channel<Int>()
    private val channel3 = Channel<Int>()
    private val channel4 = Channel<Int>()

    fun runTest() = runBlocking {
        launch { function1() }
        launch { function2() }
        launch { function3() }

        function4()
        function5()
    }

    private suspend fun function1() {
        channel1.send(1)
        channel2.receive()

        channel3.send(3)
        channel4.receive()
    }

    private suspend fun function2() {
        channel2.send(2)
        channel3.receive()

        channel4.send(4)
        channel1.receive()
    }

    private suspend fun function3() {
        channel3.send(5)
        channel1.receive()

        channel2.send(6)
        channel4.receive()
    }

    private fun function4() {
        runBlocking {
            function1()
            function2()
        }
    }

    private fun function5() {
        runBlocking {
            function3()
        }
    }
}

fun main(): Unit{
    val deadlockDetector = DeadlockDetector()
    deadlockDetector.runTest()
}

class RunChecker558: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}