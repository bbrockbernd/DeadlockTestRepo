/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":1,"nChannels":4,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 4 different channels
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
package org.example.altered.test803
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun function1(channel1: Channel<Int>, channel2: Channel<Int>) {
    GlobalScope.launch {
        for (i in 1..5) {
            channel1.send(i)
            val received = channel2.receive()
            println("Function1 received: $received")
        }
    }
}

fun function2(channel3: Channel<Int>, channel4: Channel<Int>) {
    GlobalScope.launch {
        for (i in 1..5) {
            val received = channel3.receive()
            println("Function2 received: $received")
            channel4.send(received * 2)
        }
    }
}

fun function3(channel1: Channel<Int>, channel3: Channel<Int>, channel4: Channel<Int>) {
    GlobalScope.launch {
        for (i in 1..5) {
            val value = channel1.receive()
            println("Function3 received from channel1: $value")
            channel3.send(value)
            val processedValue = channel4.receive()
            println("Function3 received from channel4: $processedValue")
        }
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()

    function1(channel1, channel2)
    function2(channel3, channel4)
    function3(channel1, channel3, channel4)

    launch {
        for (i in 1..5) {
            val value = channel1.receive()
            channel2.send(value * 3)
        }
    }

    delay(1000)
}

class RunChecker803: RunCheckerBase() {
    override fun block() = main()
}