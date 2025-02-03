/* 
{"deadlock":true,"nFunctions":2,"nCoroutines":1,"nChannels":5,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 5 different channels
- 1 different coroutines
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
package org.example.altered.test996
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()

    launch {
        functionOne(channel1, channel2, channel3)
    }

    functionTwo(channel4, channel5)
}

suspend fun functionOne(channel1: Channel<Int>, channel2: Channel<Int>, channel3: Channel<Int>) {
    channel1.send(1)
    channel2.send(channel1.receive())
    channel3.send(channel2.receive())
    channel1.receive() // Deadlock Point
}

suspend fun functionTwo(channel4: Channel<Int>, channel5: Channel<Int>) {
    channel4.send(2)
    channel5.send(channel4.receive())
    channel4.send(channel5.receive())
    channel5.receive() // Deadlock Point
}

class RunChecker996: RunCheckerBase() {
    override fun block() = main()
}