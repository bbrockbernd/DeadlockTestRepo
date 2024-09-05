/* 
{"deadlock":true,"nFunctions":2,"nCoroutines":1,"nChannels":3,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 3 different channels
- 1 different coroutines
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
package org.example.generated.test756
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class TaskManager {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()

    suspend fun producer() {
        val result = channel1.receive()
        channel2.send(result)
    }

    suspend fun consumer() {
        val result = channel2.receive()
        channel3.send(result)
    }

    fun start() = runBlocking {
        launch {
            producer()
        }
        launch {
            consumer()
        }
        channel1.send(42)
        println(channel3.receive())
    }
}

fun main(): Unit{
    TaskManager().start()
}