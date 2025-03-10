/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":4,"nChannels":5,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 5 different channels
- 4 different coroutines
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
package org.example.generated.test965
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

val channel1 = Channel<Int>()
val channel2 = Channel<Int>()
val channel3 = Channel<Int>()
val channel4 = Channel<Int>()
val channel5 = Channel<Int>()

fun function1() = runBlocking {
    launch {
        val value1 = channel1.receive()
        channel2.send(value1 + 1)
    }
}

fun function2() = runBlocking {
    launch {
        val value2 = channel2.receive()
        channel3.send(value2 + 1)
    }
}

fun function3() = runBlocking {
    launch {
        val value3 = channel3.receive()
        channel4.send(value3 + 1)
    }
}

fun function4() = runBlocking {
    launch {
        val value4 = channel4.receive()
        channel1.send(value4 + 1)
    }
}

fun main(): Unit= runBlocking {
    launch { function1() }
    launch { function2() }
    launch { function3() }
    launch { function4() }

    channel1.send(0)
}