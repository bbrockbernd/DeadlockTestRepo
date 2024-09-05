/* 
{"deadlock":true,"nFunctions":2,"nCoroutines":1,"nChannels":1,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 1 different channels
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
package org.example.generated.test926
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun function1(channel: Channel<Int>) = runBlocking {
    launch {
        coroutineScope {
            channel.send(1)
            channel.receive()
        }
    }
}

fun function2(channel: Channel<Int>) = runBlocking {
    launch {
        coroutineScope {
            channel.send(2)
            channel.receive()
        }
    }
}

fun main(): Unit= runBlocking {
    val channel = Channel<Int>()
    function1(channel)
    function2(channel)
}