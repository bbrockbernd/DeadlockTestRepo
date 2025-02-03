/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":6,"nChannels":1,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 1 different channels
- 6 different coroutines
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
package org.example.altered.test433
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun producer(channel: Channel<Int>, values: List<Int>) = runBlocking {
    values.forEach {
        channel.send(it)
    }
    channel.close()
}

fun consumer(channel: Channel<Int>, process: (Int) -> Unit) = runBlocking {
    for (value in channel) {
        process(value)
    }
}

fun intermediate(channel1: Channel<Int>, channel2: Channel<Int>) = runBlocking {
    for (value in channel1) {
        channel2.send(value * 2) // example processing: multiply by 2
    }
    channel2.close()
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()

    launch {
        producer(channel1, listOf(1, 2, 3, 4, 5))
    }
    launch {
        intermediate(channel1, channel2)
    }
    launch {
        consumer(channel2) {
            println("Received Value: $it")
        }
    }

    launch {
        producer(channel1, listOf(6, 7, 8, 9, 10))
    }
    launch {
        intermediate(channel1, channel2)
    }
    launch {
        consumer(channel2) {
            println("Received Second Batch Value: $it")
        }
    }
}

class RunChecker433: RunCheckerBase() {
    override fun block() = main()
}