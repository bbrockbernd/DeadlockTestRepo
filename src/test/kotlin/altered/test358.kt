/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":4,"nChannels":1,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 1 different channels
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
package org.example.altered.test358
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun main(): Unit= runBlocking {
    val channel = Channel<Int>(Channel.BUFFERED)

    launch { producer(channel) }
    launch { consumer(channel) }
    launch { additionalProducer(channel) }
    launch { additionalConsumer(channel) }
}

suspend fun producer(channel: Channel<Int>) {
    for (i in 1..5) {
        channel.send(i)
    }
    channel.close()  // Close the channel when done
}

suspend fun consumer(channel: Channel<Int>) {
    for (value in channel) {
        println("Consumer received: $value")
    }
}

suspend fun additionalProducer(channel: Channel<Int>) {
    for (i in 6..10) {
        channel.send(i)
    }
    channel.close()
}

suspend fun additionalConsumer(channel: Channel<Int>) {
    for (value in channel) {
        println("Additional consumer received: $value")
    }
}

class RunChecker358: RunCheckerBase() {
    override fun block() = main()
}