/* 
{"deadlock":false,"nFunctions":7,"nCoroutines":4,"nChannels":3,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
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
package org.example.altered.test11
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>(capacity = 2)
    val channel2 = Channel<Int>(capacity = 2)
    val channel3 = Channel<Int>(capacity = 2)

    launch { coroutineA(channel1) }
    launch { coroutineB(channel1, channel2) }
    launch { coroutineC(channel2, channel3) }
    launch { coroutineD(channel3) }

    functionG(channel1, channel2, channel3)
}

suspend fun coroutineA(channel1: Channel<Int>) {
    repeat(5) {
        delay(100)
        channel1.send(it)
    }
    channel1.close()
}

suspend fun coroutineB(channel1: Channel<Int>, channel2: Channel<Int>) {
    for (value in channel1) {
        channel2.send(value * 2)
    }
    channel2.close()
}

suspend fun coroutineC(channel2: Channel<Int>, channel3: Channel<Int>) {
    for (value in channel2) {
        channel3.send(value + 1)
    }
    channel3.close()
}

suspend fun coroutineD(channel3: Channel<Int>) {
    for (value in channel3) {
        println("Received value: $value")
    }
}

fun functionE(channel1: Channel<Int>, channel2: Channel<Int>, channel3: Channel<Int>) {
    runBlocking {
        coroutineA(channel1)
        coroutineB(channel1, channel2)
        coroutineC(channel2, channel3)
        coroutineD(channel3)
    }
}

fun functionF(channel1: Channel<Int>, channel2: Channel<Int>, channel3: Channel<Int>) {
    runBlocking {
        launch { coroutineA(channel1) }
        launch { coroutineB(channel1, channel2) }
        launch { coroutineC(channel2, channel3) }
        launch { coroutineD(channel3) }
    }
}

fun functionG(channel1: Channel<Int>, channel2: Channel<Int>, channel3: Channel<Int>) {
    runBlocking {
        coroutineScope {
            launch { coroutineA(channel1) }
            launch { coroutineB(channel1, channel2) }
            launch { coroutineC(channel2, channel3) }
            launch { coroutineD(channel3) }
        }
    }
}

class RunChecker11: RunCheckerBase() {
    override fun block() = main()
}