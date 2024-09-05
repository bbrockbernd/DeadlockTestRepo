/* 
{"deadlock":true,"nFunctions":1,"nCoroutines":3,"nChannels":2,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
- 2 different channels
- 3 different coroutines
- 0 different classes

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
package org.example.generated.test650
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

fun deadlockTest() = runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()

    launch {
        channel1.send(1)
        channel2.receive()
    }
    
    launch {
        channel2.send(2)
        channel1.receive()
    }

    launch {
        channel1.receive()
    }
}

fun main(): Unit{
    deadlockTest()
}