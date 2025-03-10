/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":2,"nChannels":1,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 1 different channels
- 2 different coroutines
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
package org.example.generated.test659
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class DeadlockExample(val channel: Channel<Int>)

fun function1(channel: Channel<Int>) {
    runBlocking {
        launch {
            functions4(channel)
        }
        launch {
            functions5(channel)
        }
    }
}

fun functions2(channel: Channel<Int>) = runBlocking {
    coroutineScope {
        function1(channel)
    }
}

fun functions3(channel: Channel<Int>) = runBlocking {
    channel.send(1)
    val value = channel.receive()
}

fun functions4(channel: Channel<Int>) = runBlocking {
    channel.send(2)
    val value = channel.receive()
}

fun functions5(channel: Channel<Int>) {
    runBlocking {
        launch {
            functions2(channel)
        }
        functions3(channel)
    }
}

fun main(): Unit{
    val channel = Channel<Int>()
    val deadlockExample = DeadlockExample(channel)
    functions5(deadlockExample.channel)
}