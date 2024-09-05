/* 
{"deadlock":true,"nFunctions":2,"nCoroutines":4,"nChannels":4,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 4 different channels
- 4 different coroutines
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
package org.example.generated.test415
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()

    launch { deadlockProducer(channel1, channel2) }
    launch { deadlockConsumer(channel2, channel3) }

    launch { deadlockProducer(channel3, channel4) }
    launch { deadlockConsumer(channel4, channel1) }
}

suspend fun deadlockProducer(input: Channel<Int>, output: Channel<Int>) {
    input.send(1)
    output.send(input.receive())
}

suspend fun deadlockConsumer(input: Channel<Int>, output: Channel<Int>) {
    val value = input.receive()
    output.send(value)
    output.send(2)
}