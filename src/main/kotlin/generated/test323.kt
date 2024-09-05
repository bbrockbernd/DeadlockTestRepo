/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":7,"nChannels":5,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 5 different channels
- 7 different coroutines
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
package org.example.generated.test323
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun functionOne(channelA: Channel<Int>, channelB: Channel<Int>, channelC: Channel<Int>) {
    repeat(3) {
        GlobalScope.launch {
            channelA.send(it)
        }
        GlobalScope.launch {
            val received = channelA.receive() + 1
            channelB.send(received)
        }
        GlobalScope.launch {
            val result = channelB.receive() * 2
            channelC.send(result)
        }
    }
}

fun functionTwo(channelC: Channel<Int>, channelD: Channel<Int>, channelE: Channel<Int>) {
    repeat(2) {
        GlobalScope.launch {
            val value = channelC.receive()
            channelD.send(value + 3)
        }
        GlobalScope.launch {
            val result = channelD.receive() - 3
            channelE.send(result)
        }
    }
}

fun main(): Unit= runBlocking {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()
    val channelC = Channel<Int>()
    val channelD = Channel<Int>()
    val channelE = Channel<Int>()

    functionOne(channelA, channelB, channelC)
    functionTwo(channelC, channelD, channelE)

    repeat(3) {
        println(channelE.receive())
    }
}