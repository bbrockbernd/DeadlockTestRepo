/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":5,"nChannels":2,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 2 different channels
- 5 different coroutines
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
package org.example.generated.test628
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()

    launch { function1(channel1, channel2) }
    launch { function2(channel1, channel2) }
    launch { function3(channel1, channel2) }
    launch { function4(channel1, channel2) }
    function5(channel1, channel2)
}

suspend fun function1(channel1: Channel<Int>, channel2: Channel<Int>) {
    val data = channel2.receive()
    channel1.send(data)
}

suspend fun function2(channel1: Channel<Int>, channel2: Channel<Int>) {
    val data = channel1.receive()
    channel2.send(data)
}

suspend fun function3(channel1: Channel<Int>, channel2: Channel<Int>) {
    val data = channel1.receive()
    channel2.send(data)
}

suspend fun function4(channel1: Channel<Int>, channel2: Channel<Int>) {
    val data = channel2.receive()
    channel1.send(data)
}

suspend fun function5(channel1: Channel<Int>, channel2: Channel<Int>) {
    channel1.send(1)
    channel2.send(1)
}