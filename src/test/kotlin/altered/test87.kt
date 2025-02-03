/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":4,"nChannels":3,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 3 different channels
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
package org.example.altered.test87
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun producerOne(channelOne: Channel<Int>) {
    GlobalScope.launch {
        for (i in 1..5) {
            channelOne.send(i)
        }
        channelOne.close()
    }
}

fun producerTwo(channelTwo: Channel<Int>) {
    GlobalScope.launch {
        for (i in 6..10) {
            channelTwo.send(i)
        }
        channelTwo.close()
    }
}

fun forwarder(channelOne: Channel<Int>, channelThree: Channel<Int>) {
    GlobalScope.launch {
        for (element in channelOne) {
            channelThree.send(element * 2)
        }
    }
}

fun consumer(channelThree: Channel<Int>) {
    GlobalScope.launch {
        for (element in channelThree) {
            println("Consuming: $element")
        }
    }
}

fun main(): Unit= runBlocking {
    val channelOne = Channel<Int>()
    val channelTwo = Channel<Int>()
    val channelThree = Channel<Int>()

    producerOne(channelOne)
    producerTwo(channelTwo)
    forwarder(channelOne, channelThree)
    forwarder(channelTwo, channelThree)
    consumer(channelThree)

    // Add a delay to allow coroutines to complete
    delay(2000)
}

class RunChecker87: RunCheckerBase() {
    override fun block() = main()
}