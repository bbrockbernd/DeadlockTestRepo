/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":4,"nChannels":2,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 2 different channels
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
package org.example.altered.test944
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun main(): Unit= runBlocking {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()

    launchDeadlock1(channelA, channelB)
    launchDeadlock2(channelA, channelB)
    launchDeadlock3(channelA, channelB)
    launchDeadlock4(channelA, channelB)
}

fun CoroutineScope.launchDeadlock1(channelA: Channel<Int>, channelB: Channel<Int>) = launch {
    val valueA = channelA.receive()
    channelB.send(valueA)
}

fun CoroutineScope.launchDeadlock2(channelA: Channel<Int>, channelB: Channel<Int>) = launch {
    val valueB = channelB.receive()
    channelA.send(valueB)
}

fun CoroutineScope.launchDeadlock3(channelA: Channel<Int>, channelB: Channel<Int>) = launch {
    channelA.send(1)
    val received = channelB.receive()
}

fun CoroutineScope.launchDeadlock4(channelA: Channel<Int>, channelB: Channel<Int>) = launch {
    channelB.send(2)
    val received = channelA.receive()
}

class RunChecker944: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}