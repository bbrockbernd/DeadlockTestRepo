/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":2,"nChannels":3,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 3 different channels
- 2 different coroutines
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
package org.example.generated.test967
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class DeadlockTester {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()

    fun start() = runBlocking {
        launch {
            coroutine1()
        }
        launch {
            coroutine2()
        }
    }

    suspend fun coroutine1() {
        function1()
        function2()
    }

    suspend fun coroutine2() {
        function3()
        function4()
    }

    suspend fun function1() {
        channel1.send(1)
        channel2.receive()
    }

    suspend fun function2() {
        channel2.send(2)
        channel3.receive()
    }

    suspend fun function3() {
        channel3.send(3)
        channel1.receive()
    }

    suspend fun function4() {
        channel2.send(4)
    }
}

fun main(): Unit{
    val tester = DeadlockTester()
    tester.start()
}