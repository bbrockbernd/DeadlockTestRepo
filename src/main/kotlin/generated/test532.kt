/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":5,"nChannels":1,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 1 different channels
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
package org.example.generated.test532
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.coroutineScope

suspend fun function1(channel: Channel<Int>) {
    coroutineScope {
        launch { channel.send(1) }
        launch { channel.send(2) }
    }
}

suspend fun function2(channel: Channel<Int>) {
    coroutineScope {
        launch { val x = channel.receive() }
        launch { channel.send(3) }
    }
}

suspend fun function3(channel: Channel<Int>) {
    coroutineScope {
        launch { val y = channel.receive() }
        launch { val z = channel.receive() }
    }
}

suspend fun function4(channel: Channel<Int>) {
    coroutineScope {
        launch { channel.send(4) }
        launch { val w = channel.receive() }
    }
}

fun main(): Unit= runBlocking {
    val channel = Channel<Int>()

    launch { function1(channel) }
    launch { function2(channel) }
    launch { function3(channel) }
    launch { function4(channel) }
    launch { val a = channel.receive() }
}