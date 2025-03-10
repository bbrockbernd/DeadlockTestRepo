/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":2,"nChannels":2,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 2 different channels
- 2 different coroutines
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
package org.example.generated.test602
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun main(): Unit= runBlocking<Unit> {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    
    launch { 
        producer(channel1) 
    }
    
    launch { 
        consumer(channel1, channel2) 
    }

    coroutineScope {
        receiver(channel2)
    }
}

suspend fun producer(channel: Channel<Int>) {
    for (i in 1..5) {
        channel.send(i)
    }
    channel.close()
}

suspend fun consumer(inputChannel: Channel<Int>, outputChannel: Channel<Int>) {
    for (i in inputChannel) {
        outputChannel.send(i * 2)
    }
    outputChannel.close()
}

suspend fun receiver(channel: Channel<Int>) {
    for (i in channel) {
        println(i)
    }
}