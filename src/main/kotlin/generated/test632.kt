/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":2,"nChannels":4,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 4 different channels
- 2 different coroutines
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
package org.example.generated.test632
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

val channel1 = Channel<Int>()
val channel2 = Channel<Int>()
val channel3 = Channel<Int>()
val channel4 = Channel<Int>()

fun functionA() = runBlocking {
    launch {
        val value = channel1.receive()
        channel2.send(value)
    }
}

fun functionB() = runBlocking {
    launch {
        val value = channel2.receive()
        channel3.send(value)
    }
}

fun functionC() = runBlocking {
    launch {
        val value = channel3.receive()
        channel4.send(value)
    }
}

fun functionD() = runBlocking {
    launch {
        val value = channel4.receive()
        channel1.send(value)
    }
}

fun main(): Unit= runBlocking {
    launch { functionA() }
    launch { functionB() }
    launch { functionC() }
    launch { functionD() }
}