/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":3,"nChannels":3,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 3 different channels
- 3 different coroutines
- 4 different classes

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
package org.example.altered.test475
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ResourceA(val channel: Channel<Int>)
class ResourceB(val channel: Channel<Int>)
class ResourceC(val channel: Channel<Int>)
class ResourceD()
class ResourceE()

fun producer(channel: Channel<Int>, start: Int, end: Int) {
    runBlocking {
        for (i in start..end) {
            channel.send(i)
        }
        channel.close()
    }
}

suspend fun consumerA(channel: Channel<Int>, resourceA: ResourceA) {
    for (msg in channel) {
        println("ConsumerA received: $msg")
    }
}

suspend fun consumerB(channel: Channel<Int>, resourceB: ResourceB) {
    for (msg in channel) {
        println("ConsumerB received: $msg")
    }
}

suspend fun coordinator(resourceA: ResourceA, resourceB: ResourceB, resourceC: ResourceC) {
    coroutineScope {
        launch {
            consumerA(resourceA.channel, resourceA)
        }
        launch {
            consumerB(resourceB.channel, resourceB)
        }
    }
}

fun main(): Unit{
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()

    val resourceA = ResourceA(channel1)
    val resourceB = ResourceB(channel2)
    val resourceC = ResourceC(Channel())
    val resourceD = ResourceD()
    val resourceE = ResourceE()

    runBlocking {
        launch {
            producer(resourceA.channel, 1, 5)
        }
        launch {
            producer(resourceB.channel, 6, 10)
        }
        launch {
            coordinator(resourceA, resourceB, resourceC)
        }
    }
}

class RunChecker475: RunCheckerBase() {
    override fun block() = main()
}