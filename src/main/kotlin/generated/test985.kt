/* 
{"deadlock":true,"nFunctions":1,"nCoroutines":1,"nChannels":3,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
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
package org.example.generated.test985
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class DeadlockExample {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()

    fun exampleFunction() = runBlocking {
        val job = launch {
            coroutineFunction()
        }
        job.join() // Waiting for the coroutine to finish
    }

    private suspend fun coroutineFunction() = coroutineScope {
        launch {
            channel1.send(1) // This line will block
            channel2.receive() // Waiting to receive from channel2
        }

        launch {
            channel2.send(2) // This line will block
            channel3.receive() // Waiting to receive from channel3
        }

        launch {
            channel3.send(3) // This line will block
            channel1.receive() // Waiting to receive from channel1
        }
    }
}

fun main(): Unit{
    val deadlockExample = DeadlockExample()
    deadlockExample.exampleFunction() // This will cause a deadlock
}