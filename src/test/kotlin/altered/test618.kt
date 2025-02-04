/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":1,"nChannels":1,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 1 different channels
- 1 different coroutines
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
package org.example.altered.test618
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class SimpleClass(val channel: Channel<String>) {
    suspend fun sendData() {
        channel.send("Hello")
    }
}

suspend fun receiveData(channel: Channel<String>): String {
    return channel.receive()
}

fun main(): Unit= runBlocking {
    val channel = Channel<String>()

    val simpleClass = SimpleClass(channel)

    launch {
        simpleClass.sendData()
    }

    val message = receiveData(channel)
    println(message)
}

class RunChecker618: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}