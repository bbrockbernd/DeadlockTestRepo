/* 
{"deadlock":true,"nFunctions":2,"nCoroutines":1,"nChannels":4,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 4 different channels
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
package org.example.generated.test533
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun function1(channelA: Channel<Int>, channelB: Channel<Int>) = runBlocking {
    launch {
        val valueA = channelA.receive()
        channelB.send(valueA)
    }
    channelA.send(42) // This send will be blocked because channel is unbuffered and nothing is ready to receive at this point
}

fun function2(channelC: Channel<Int>, channelD: Channel<Int>) = runBlocking {
    launch {
        val valueC = channelC.receive()
        channelD.send(valueC)
    }
    channelC.send(24) // This send will be blocked because channel is unbuffered and nothing is ready to receive at this point
}

fun main(): Unit= runBlocking {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()
    val channelC = Channel<Int>()
    val channelD = Channel<Int>()

    launch {
        function1(channelA, channelB)
    }

    launch {
        function2(channelC, channelD)
    }

}