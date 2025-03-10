/* 
{"deadlock":true,"nFunctions":2,"nCoroutines":2,"nChannels":5,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 5 different channels
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
package org.example.generated.test677
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class DeadlockExample {
    private val channel1 = Channel<Int>()
    private val channel2 = Channel<Int>()
    private val channel3 = Channel<Int>()
    private val channel4 = Channel<Int>()
    private val channel5 = Channel<Int>()

    suspend fun functionOne() {
        val value = channel1.receive()
        channel2.send(value)
        channel5.send(channel3.receive())
    }

    suspend fun functionTwo() {
        val value = channel4.receive()
        channel3.send(value)
        channel1.send(channel5.receive())
    }

    fun startCoroutines() = runBlocking {
        launch {
            functionOne()
        }

        launch {
            functionTwo()
        }

        channel4.send(42)  // Starting point to initiate the process
    }
}

fun main(): Unit{
    DeadlockExample().startCoroutines()
}