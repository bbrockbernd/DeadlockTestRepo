/* 
{"deadlock":false,"nFunctions":7,"nCoroutines":6,"nChannels":2,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 2 different channels
- 6 different coroutines
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
package org.example.altered.test31
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>(5)

    launch { coroutine1(channel1, channel2) }
    launch { coroutine2(channel1) }
    launch { coroutine3(channel2) }
    launch { coroutine4(channel2) }
    launch { coroutine5(channel1) }
    coroutine6(channel2)

    delay(1000L) // Wait for coroutines to process
    coroutine7(channel1, channel2)
}

suspend fun coroutine1(channel1: Channel<Int>, channel2: Channel<Int>) {
    for (i in 1..5) {
        channel1.send(i)
    }
    for (i in 6..10) {
        channel2.send(i)
    }
}

suspend fun coroutine2(channel1: Channel<Int>) {
    for (i in 1..5) {
        println("coroutine2 received: ${channel1.receive()}")
    }
}

suspend fun coroutine3(channel2: Channel<Int>) {
    for (i in 1..3) {
        println("coroutine3 received: ${channel2.receive()}")
    }
}

suspend fun coroutine4(channel2: Channel<Int>) {
    for (i in 4..5) {
        println("coroutine4 received: ${channel2.receive()}")
    }
}

suspend fun coroutine5(channel1: Channel<Int>) {
    println("coroutine5 received: ${channel1.receive()}")
}

suspend fun coroutine6(channel2: Channel<Int>) {
    println("coroutine6 received: ${channel2.receive()}")
}

fun coroutine7(channel1: Channel<Int>, channel2: Channel<Int>) {
    runBlocking {
        channel1.close()
        channel2.close()
    }
}

class RunChecker31: RunCheckerBase() {
    override fun block() = main()
}