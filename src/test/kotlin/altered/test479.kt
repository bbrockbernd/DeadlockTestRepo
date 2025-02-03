/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":3,"nChannels":2,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 2 different channels
- 3 different coroutines
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
package org.example.altered.test479
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()

    val job1 = launch {
        producer(channel1)
    }

    val job2 = launch {
        middleman(channel1, channel2)
    }

    val job3 = launch {
        consumer(channel2)
    }

    // Let the coroutines run for a while
    delay(1000)
    
    job1.cancel()
    job2.cancel()
    job3.cancel()
}

suspend fun producer(channel: Channel<Int>) {
    var x = 0
    while (true) {
        channel.send(x++)
        delay(200)
    }
}

suspend fun middleman(input: Channel<Int>, output: Channel<Int>) {
    while (true) {
        val value = input.receive()
        output.send(value * 2)
        delay(200)
    }
}

suspend fun consumer(channel: Channel<Int>) {
    while (true) {
        val value = channel.receive()
        println("Received: $value")
        delay(200)
    }
}

class RunChecker479: RunCheckerBase() {
    override fun block() = main()
}