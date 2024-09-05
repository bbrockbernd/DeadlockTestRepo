/* 
{"deadlock":true,"nFunctions":7,"nCoroutines":1,"nChannels":5,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 5 different channels
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
package org.example.generated.test6
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun main(): Unit= runBlocking {
    val chan1 = Channel<Int>()
    val chan2 = Channel<Int>()
    val chan3 = Channel<Int>()
    val chan4 = Channel<Int>()
    val chan5 = Channel<Int>()

    launch { function1(chan1, chan2) }
    launch { function2(chan2, chan3) }
    launch { function3(chan3, chan4) }
    launch { function4(chan4, chan5) }
    launch { function5(chan5, chan1) }
    launch { function6(chan1, chan3, chan5) }
    launch { function7(chan2, chan4) }
}

suspend fun function1(chan1: Channel<Int>, chan2: Channel<Int>) {
    val result = chan1.receive()
    chan2.send(result)
}

suspend fun function2(chan2: Channel<Int>, chan3: Channel<Int>) {
    val result = chan2.receive()
    chan3.send(result)
}

suspend fun function3(chan3: Channel<Int>, chan4: Channel<Int>) {
    val result = chan3.receive()
    chan4.send(result)
}

suspend fun function4(chan4: Channel<Int>, chan5: Channel<Int>) {
    val result = chan4.receive()
    chan5.send(result)
}

suspend fun function5(chan5: Channel<Int>, chan1: Channel<Int>) {
    val result = chan5.receive()
    chan1.send(result)
}

suspend fun function6(chan1: Channel<Int>, chan3: Channel<Int>, chan5: Channel<Int>) {
    chan1.send(1)
    val result = chan3.receive() + chan5.receive()
}

suspend fun function7(chan2: Channel<Int>, chan4: Channel<Int>) {
    chan2.send(2)
    val result = chan4.receive()
}