/* 
{"deadlock":false,"nFunctions":1,"nCoroutines":3,"nChannels":2,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
- 2 different channels
- 3 different coroutines
- 2 different classes

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
package org.example.generated.test755
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA(private val channel: Channel<Int>) {
    suspend fun sendData() {
        repeat(5) {
            channel.send(it)
        }
    }
}

class ClassB(private val inputChannel: Channel<Int>, private val outputChannel: Channel<Int>) {
    suspend fun processData() {
        for (i in 1..5) {
            val receivedValue = inputChannel.receive()
            outputChannel.send(receivedValue * 2)
        }
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val classA = ClassA(channel1)
    val classB = ClassB(channel1, channel2)

    launch {
        classA.sendData()
    }

    launch {
        classB.processData()
    }

    launch {
        for (i in 1..5) {
            println("Received: ${channel2.receive()}")
        }
    }
}