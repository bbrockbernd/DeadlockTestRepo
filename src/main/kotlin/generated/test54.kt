/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":7,"nChannels":4,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 4 different channels
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
package org.example.generated.test54
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun main(): Unit= runBlocking {
    val channelA = Channel<Int>(2)
    val channelB = Channel<Int>(2)
    val channelC = Channel<Int>()
    val channelD = Channel<Int>(2)

    launch { function1(channelA, channelB) }
    launch { function2(channelB, channelC) }
    launch { function3(channelC, channelD) }
    launch { function4(channelA) }
    launch { function5(channelB) }
    launch { receiver(channelD) }
    launch { receiver(channelC) }
}

suspend fun function1(channelA: Channel<Int>, channelB: Channel<Int>) {
    repeat(5) {
        channelA.send(it)
        channelB.send(it)
    }
    channelA.close()
    channelB.close()
}

suspend fun function2(channelB: Channel<Int>, channelC: Channel<Int>) {
    for (x in channelB) {
        channelC.send(x * 2)
    }
    channelC.close()
}

suspend fun function3(channelC: Channel<Int>, channelD: Channel<Int>) {
    for (x in channelC) {
        channelD.send(x + 1)
    }
    channelD.close()
}

suspend fun function4(channelA: Channel<Int>) {
    for (x in channelA) {
        println("Function 4 received from channelA: $x")
    }
}

suspend fun function5(channelB: Channel<Int>) {
    for (x in channelB) {
        println("Function 5 received from channelB: $x")
    }
}

suspend fun receiver(channel: Channel<Int>) {
    for (x in channel) {
        println("Receiver consumed from channel: $x")
    }
}