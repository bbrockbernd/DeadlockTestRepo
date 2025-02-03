/* 
{"deadlock":true,"nFunctions":2,"nCoroutines":4,"nChannels":6,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 6 different channels
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
package org.example.altered.test264
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()
    val channel6 = Channel<Int>()

    launch { coroutineOne(channel1, channel2, channel5) }
    launch { coroutineTwo(channel2, channel3) }
    launch { coroutineThree(channel3, channel4) }
    launch { coroutineFour(channel4, channel1, channel6) }
    
    // This line is essential to keep the main function running until all launched coroutines complete.
    delay(1000L)
}

fun coroutineOne(ch1: Channel<Int>, ch2: Channel<Int>, ch5: Channel<Int>) = runBlocking {
    ch1.send(1)
    val value = ch2.receive()
    ch5.send(value)
}

fun coroutineTwo(ch2: Channel<Int>, ch3: Channel<Int>) = runBlocking {
    val value = ch2.receive()
    ch3.send(value)
}

fun coroutineThree(ch3: Channel<Int>, ch4: Channel<Int>) = runBlocking {
    val value = ch3.receive()
    ch4.send(value)
}

fun coroutineFour(ch4: Channel<Int>, ch1: Channel<Int>, ch6: Channel<Int>) = runBlocking {
    val value = ch4.receive()
    ch1.send(value)
    ch6.send(42) // Arbitrary value to possibly unblock channel 6
}

class RunChecker264: RunCheckerBase() {
    override fun block() = main()
}