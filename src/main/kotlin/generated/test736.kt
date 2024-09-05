/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":4,"nChannels":2,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 2 different channels
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
package org.example.generated.test736
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun functionA(channel1: Channel<Int>) = GlobalScope.launch {
    for (i in 1..5) {
        channel1.send(i)
    }
    channel1.close()
}

fun functionB(channel1: Channel<Int>, channel2: Channel<Int>) = GlobalScope.launch {
    for (i in channel1) {
        channel2.send(i * 2)
    }
    channel2.close()
}

fun functionC(channel2: Channel<Int>) = GlobalScope.launch {
    for (i in channel2) {
        println("Received from channel2: $i")
    }
}

fun functionD() = runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()

    functionA(channel1)
    functionB(channel1, channel2)
    functionC(channel2)
    delay(1000L)
}

fun main(): Unit{
    functionD()
}