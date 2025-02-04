/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":2,"nChannels":3,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 3 different channels
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
package org.example.altered.test848
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()

    launch { coroutine1(channel1, channel2) }
    launch { coroutine2(channel2, channel3) }

    function1(channel1)
    function2(channel3)
    function3(channel1)
}

fun function1(channel: Channel<Int>) {
    runBlocking {
        channel.send(5)
    }
}

fun function2(channel: Channel<Int>) {
    runBlocking {
        val value = channel.receive()
    }
}

suspend fun function3(channel: Channel<Int>) {
    coroutineScope {
        val value = function4(channel)
        function5(channel, value)
    }
}

suspend fun function4(channel: Channel<Int>): Int {
    return channel.receive()
}

suspend fun function5(channel: Channel<Int>, value: Int) {
    channel.send(value)
}

suspend fun coroutine1(channel1: Channel<Int>, channel2: Channel<Int>) {
    val value = channel1.receive()
    channel2.send(value)
}

suspend fun coroutine2(channel2: Channel<Int>, channel3: Channel<Int>) {
    val value = channel2.receive()
    channel3.send(value)
}

class RunChecker848: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}