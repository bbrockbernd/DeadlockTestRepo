/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":1,"nChannels":5,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
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
package org.example.altered.test718
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun function1(channel1: Channel<Int>, channel2: Channel<Int>) = runBlocking {
    launch {
        val value = channel1.receive()
        channel2.send(value)
    }
}

fun function2(channel3: Channel<Int>, channel4: Channel<Int>) = runBlocking {
    launch {
        val value = channel3.receive()
        channel4.send(value)
    }
}

fun function3(channel2: Channel<Int>, channel3: Channel<Int>) = runBlocking {
    launch {
        val value = channel2.receive()
        channel3.send(value)
    }
}

fun function4(channel4: Channel<Int>, channel5: Channel<Int>) = runBlocking {
    launch {
        val value = channel4.receive()
        channel5.send(value)
    }
}

fun function5(channel5: Channel<Int>) = runBlocking {
    launch {
        val value = channel5.receive()
        println(value)
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()

    function1(channel1, channel2)
    function2(channel3, channel4)
    function3(channel2, channel3)
    function4(channel4, channel5)
    function5(channel5)

    channel1.send(42)
}

class RunChecker718: RunCheckerBase() {
    override fun block() = main()
}