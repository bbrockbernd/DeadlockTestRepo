/* 
{"deadlock":true,"nFunctions":1,"nCoroutines":4,"nChannels":4,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
- 4 different channels
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
package org.example.generated.test791
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class DeadlockTest {

    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()

    fun createDeadlock() = runBlocking {
        launch { coroutineA() }
        launch { coroutineB() }
        launch { coroutineC() }
        launch { coroutineD() }
    }

    private suspend fun coroutineA() {
        channel1.send(1)
        channel2.receive()
    }

    private suspend fun coroutineB() {
        channel2.send(2)
        channel3.receive()
    }

    private suspend fun coroutineC() {
        channel3.send(3)
        channel4.receive()
    }

    private suspend fun coroutineD() {
        channel4.send(4)
        channel1.receive()
    }
}

fun main(): Unit{
    val test = DeadlockTest()
    test.createDeadlock()
}