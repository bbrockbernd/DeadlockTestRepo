/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":8,"nChannels":2,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 2 different channels
- 8 different coroutines
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
package org.example.generated.test472
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class DeadlockTest {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()

    fun runDeadlockTest() = runBlocking {
        launch { coroutineOne() }
        launch { coroutineTwo() }
        launch { coroutineThree() }
        launch { coroutineFour() }
        launch { coroutineFive() }
        launch { coroutineSix() }
        launch { coroutineSeven() }
        launch { coroutineEight() }
    }

    private suspend fun coroutineOne() {
        channel1.send(1)
        channel2.receive()
    }

    private suspend fun coroutineTwo() {
        channel2.send(2)
        channel1.receive()
    }

    private suspend fun coroutineThree() {
        channel1.send(3)
        channel2.receive()
    }

    private suspend fun coroutineFour() {
        channel2.send(4)
        channel1.receive()
    }

    private suspend fun coroutineFive() {
        channel1.send(5)
        channel2.receive()
    }

    private suspend fun coroutineSix() {
        channel2.send(6)
        channel1.receive()
    }

    private suspend fun coroutineSeven() {
        channel1.send(7)
        channel2.receive()
    }

    private suspend fun coroutineEight() {
        channel2.send(8)
        channel1.receive()
    }
}

fun main(): Unit{
    DeadlockTest().runDeadlockTest()
}