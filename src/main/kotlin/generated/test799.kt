/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":5,"nChannels":2,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 2 different channels
- 5 different coroutines
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
package org.example.generated.test799
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class Worker(val inputChannel: ReceiveChannel<Int>, val outputChannel: SendChannel<Int>) {
    suspend fun process() {
        for (value in inputChannel) {
            outputChannel.send(value * 2)
        }
    }
}

suspend fun generator(channel: SendChannel<Int>) {
    for (i in 1..5) {
        channel.send(i)
    }
    channel.close()
}

suspend fun receiver(channel: ReceiveChannel<Int>) {
    for (i in channel) {
        println("Received: $i")
    }
}

fun main(): Unit= runBlocking {
    val inputChannel = Channel<Int>()
    val outputChannel = Channel<Int>()

    coroutineScope {
        launch { generator(inputChannel) }
        launch { Worker(inputChannel, outputChannel).process() }
        launch { receiver(outputChannel) }
    }
}