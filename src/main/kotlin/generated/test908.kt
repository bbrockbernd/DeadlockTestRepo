/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":1,"nChannels":3,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 3 different channels
- 1 different coroutines
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
package org.example.generated.test908
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun functionOne(channel1: Channel<Int>, channel2: Channel<Int>) = runBlocking {
    launch {
        val value = channel1.receive()
        channel2.send(value * 2)
    }
}

fun functionTwo(channel2: Channel<Int>, channel3: Channel<Int>) = runBlocking {
    launch {
        val value = channel2.receive()
        channel3.send(value + 1)
    }
}

fun functionThree(channel3: Channel<Int>) = runBlocking {
    launch {
        val value = channel3.receive()
        println("Final Result: $value")
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    
    functionOne(channel1, channel2)
    functionTwo(channel2, channel3)
    functionThree(channel3)

    channel1.send(5)
}

main()