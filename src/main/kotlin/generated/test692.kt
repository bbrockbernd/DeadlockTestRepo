/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":3,"nChannels":2,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 2 different channels
- 3 different coroutines
- 1 different classes

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
package org.example.generated.test692
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class DeadlockExample(val channel1: Channel<Int>, val channel2: Channel<Int>)

suspend fun function1(channel1: Channel<Int>, channel2: Channel<Int>) {
    val value = channel1.receive()
    channel2.send(value)
}

suspend fun function2(channel1: Channel<Int>, channel2: Channel<Int>) {
    val value = channel2.receive()
    channel1.send(value)
}

suspend fun function3(channel1: Channel<Int>) {
    val value = channel1.receive()
    channel1.send(value)
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val example = DeadlockExample(channel1, channel2)

    launch {
        function1(example.channel1, example.channel2)
    }

    launch {
        function2(example.channel1, example.channel2)
    }

    launch {
        function3(example.channel1)
    }

    example.channel1.send(1)  // Start the process
}