/* 
{"deadlock":true,"nFunctions":2,"nCoroutines":5,"nChannels":4,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
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
package org.example.generated.test556
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class DeadlockExample {
    val channel1 = Channel<Int>() // Unbuffered channel
    val channel2 = Channel<Int>(1) // Buffered channel with capacity of 1
    val channel3 = Channel<Int>() 
    val channel4 = Channel<Int>(1) 

    suspend fun functionA() {
        coroutineScope {
            launch {
                channel1.send(1)
                channel3.send(channel1.receive() + 1)
            }
            launch {
                channel2.send(channel3.receive() + 1)
            }
            launch {
                channel4.send(channel2.receive() + 1)
            }
        }
    }

    suspend fun functionB() {
        coroutineScope {
            launch {
                channel1.receive()
            }
            launch {
                channel2.receive()
            }
        }
    }
}

fun main(): Unit= runBlocking {
    val deadlockExample = DeadlockExample()
    launch { deadlockExample.functionA() }
    delay(1000L) // Ensure coroutine schedules appropriately
    launch { deadlockExample.functionB() }
}