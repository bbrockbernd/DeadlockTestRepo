/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":5,"nChannels":3,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 3 different channels
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
package org.example.altered.test668
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()

    launch { sendData1(channel1) }
    launch { receiveData1(channel1, channel2) }
    launch { sendData2(channel2) }
    launch { receiveData2(channel2, channel3) }
    launch { processData(channel3) }
}

suspend fun sendData1(channel: Channel<Int>) {
    repeat(5) {
        channel.send(it)
    }
    channel.close()
}

suspend fun receiveData1(channel1: Channel<Int>, channel2: Channel<Int>) {
    for (value in channel1) {
        channel2.send(value * 2)
    }
    channel2.close()
}

suspend fun sendData2(channel: Channel<Int>) {
    repeat(5) {
        channel.send(it + 10)
    }
    channel.close()
}

suspend fun receiveData2(channel1: Channel<Int>, channel2: Channel<Int>) {
    for (value in channel1) {
        channel2.send(value + 3)
    }
    channel2.close()
}

suspend fun processData(channel: Channel<Int>) {
    for (value in channel) {
        println("Processed value: $value")
    }
}

class RunChecker668: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}