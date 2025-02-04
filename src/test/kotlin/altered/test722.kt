/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":5,"nChannels":5,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 5 different channels
- 5 different coroutines
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
package org.example.altered.test722
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()

    launch { function1(channel1) }
    launch { function2(channel2, channel1) }
    launch { function3(channel3, channel4) }
    launch { function4(channel4, channel2) }
    launch { function5(channel5, channel3) }

    channel1.send(1)
    channel2.send(2)
    channel3.send(3)
    channel4.send(4)
    channel5.send(5)
}

suspend fun function1(channel: Channel<Int>) {
    val data = channel.receive()
    channel.send(data)
}

suspend fun function2(channelOut: Channel<Int>, channelIn: Channel<Int>) {
    val data = channelIn.receive()
    channelOut.send(data)
}

suspend fun function3(channelIn: Channel<Int>, channelOut: Channel<Int>) {
    val data = channelIn.receive()
    channelOut.send(data)
}

suspend fun function4(channelOut: Channel<Int>, channelIn: Channel<Int>) {
    val data = channelIn.receive()
    channelOut.send(data)
}

suspend fun function5(channelOut: Channel<Int>, channelIn: Channel<Int>) {
    val data = channelIn.receive()
    channelOut.send(data)
}

class Class1(val channel: Channel<Int>) {
    suspend fun process() {
        val data = channel.receive()
        channel.send(data)
    }
}

class Class2(val channel: Channel<Int>) {
    suspend fun process() {
        val data = channel.receive()
        channel.send(data)
    }
}

class RunChecker722: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}