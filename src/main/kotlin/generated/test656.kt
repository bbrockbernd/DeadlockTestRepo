/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":1,"nChannels":4,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
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
package org.example.generated.test656
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

suspend fun function1(channel1: Channel<Int>, channel2: Channel<Int>) {
    val value = channel1.receive()
    channel2.send(value)
}

suspend fun function2(channel3: Channel<Int>, channel4: Channel<Int>) {
    val value = channel3.receive()
    channel4.send(value)
}

suspend fun function3(channel1: Channel<Int>, channel3: Channel<Int>, channel4: Channel<Int>) {
    channel1.send(42)
    val value = channel4.receive()
    channel3.send(value)
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
   
    launch {
        function1(channel1, channel2)
    }
   
    launch {
        function2(channel3, channel4)
    }
   
    launch {
        function3(channel1, channel3, channel4)
    }
}