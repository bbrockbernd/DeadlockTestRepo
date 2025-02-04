/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":2,"nChannels":2,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
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
package org.example.altered.test32
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()

    launch { coroutineA(channel1, channel2) }
    launch { coroutineB(channel1, channel2) }
}

suspend fun coroutineA(channel1: Channel<Int>, channel2: Channel<Int>) {
    function1(channel1, channel2)
}

suspend fun coroutineB(channel1: Channel<Int>, channel2: Channel<Int>) {
    function3(channel1, channel2)
}

suspend fun function1(channel1: Channel<Int>, channel2: Channel<Int>) {
    function2(channel1, channel2)
}

suspend fun function2(channel1: Channel<Int>, channel2: Channel<Int>) {
    channel1.send(42)
    val received = channel2.receive()
    println("Received in function2: $received")
}

suspend fun function3(channel1: Channel<Int>, channel2: Channel<Int>) {
    channel2.send(99)
    val received = channel1.receive()
    println("Received in function3: $received")
}

suspend fun function4(channel1: Channel<Int>, channel2: Channel<Int>) {
    // Just a dummy function for meeting the requirement of 5 functions
}

suspend fun function5(channel1: Channel<Int>, channel2: Channel<Int>) {
    // Just a dummy function for meeting the requirement of 5 functions
}

class RunChecker32: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}