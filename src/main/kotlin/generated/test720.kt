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
package org.example.generated.test720
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class DeadlockExample {

    private val channel1 = Channel<Int>()
    private val channel2 = Channel<Int>()
    private val channel3 = Channel<Int>()

    fun start() = runBlocking {
        launch { coroutine1() }
        launch { coroutine2() }
        launch { coroutine3() }
        launch { coroutine4() }
    }

    private suspend fun coroutine1() {
        val data = channel1.receive()
        channel2.send(data)
    }

    private suspend fun coroutine2() {
        val data = channel2.receive()
        channel3.send(data)
    }

    private suspend fun coroutine3() {
        val data = channel3.receive()
        channel1.send(data) 
    }

    private suspend fun coroutine4() {
        channel1.send(1) 
    }
}

fun main(): Unit{
    val example = DeadlockExample()
    example.start()
}