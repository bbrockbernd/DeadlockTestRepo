/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":1,"nChannels":6,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 6 different channels
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
package org.example.altered.test277
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Processor(val inputCh1: Channel<Int>, val inputCh2: Channel<Int>, val outputCh: Channel<Int>) {
    suspend fun processData() {
        val data1 = inputCh1.receive()
        val data2 = inputCh2.receive()
        outputCh.send(data1 + data2)
    }
}

fun createChannels(): List<Channel<Int>> {
    val ch1 = Channel<Int>()
    val ch2 = Channel<Int>()
    val ch3 = Channel<Int>()
    val ch4 = Channel<Int>()
    val ch5 = Channel<Int>()
    val ch6 = Channel<Int>()
    return listOf(ch1, ch2, ch3, ch4, ch5, ch6)
}

suspend fun launchProcessing(proc: Processor) {
    proc.processData()
}

fun main(): Unit= runBlocking {
    val channels = createChannels()
    val processor = Processor(channels[0], channels[1], channels[2])

    val job = launch {
        launchProcessing(processor)
    }

    channels[0].send(3)
    channels[1].send(4)
    println("Result: ${channels[2].receive()}")

    channels[3].send(5)
    channels[4].send(6)
    val processor2 = Processor(channels[3], channels[4], channels[5])
    launchProcessing(processor2)
    println("Result: ${channels[5].receive()}")
}

class RunChecker277: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}