/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":5,"nChannels":5,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 5 different channels
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
package org.example.generated.test538
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Processor(val input1: Channel<Int>, val input2: Channel<Int>, val output: Channel<Int>) {
    suspend fun process() {
        val value1 = input1.receive()
        val value2 = input2.receive()
        output.send(value1 + value2)
    }
}

fun main(): Unit= runBlocking {
    val channelA = Channel<Int>(1)
    val channelB = Channel<Int>(1)
    val channelC = Channel<Int>(1)
    val channelD = Channel<Int>(1)
    val channelE = Channel<Int>(1)

    fun coroutine1(channel1: Channel<Int>, channel2: Channel<Int>) = launch {
        channel1.send(1)
        val value = channel2.receive()
        println("Coroutine1 received: $value")
    }

    fun coroutine2(channel1: Channel<Int>, channel2: Channel<Int>) = launch {
        val value = channel1.receive()
        channel2.send(value)
        println("Coroutine2 sent: $value")
    }

    fun coroutine3(processor: Processor) = launch {
        processor.process()
        val output = processor.output.receive()
        println("Coroutine3 processed: $output")
    }

    fun coroutine4(channel: Channel<Int>) = launch {
        val value = channel.receive()
        println("Coroutine4 received: $value")
    }

    coroutine1(channelA, channelB)
    coroutine2(channelA, channelC)
    coroutine2(channelD, channelB)
    coroutine4(channelE)

    val processor = Processor(channelC, channelD, channelE)
    coroutine3(processor)
}