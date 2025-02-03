/* 
{"deadlock":true,"nFunctions":7,"nCoroutines":7,"nChannels":4,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 4 different channels
- 7 different coroutines
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
package org.example.altered.test68
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()

    launch { coroutine1(channel1, channel2) }
    launch { coroutine2(channel2, channel3) }
    launch { coroutine3(channel3, channel4) }
    launch { coroutine4(channel4, channel1) }

    launch { coroutine5(channel1) }
    launch { coroutine6(channel2) }
    launch { coroutine7(channel3) }
}

suspend fun coroutine1(channel1: Channel<Int>, channel2: Channel<Int>) {
    channel1.send(1)
    val received = channel2.receive()
    println("coroutine1 received: $received")
}

suspend fun coroutine2(channel2: Channel<Int>, channel3: Channel<Int>) {
    val received = channel2.receive()
    channel3.send(received + 1)
    println("coroutine2 received and sent: ${received + 1}")
}

suspend fun coroutine3(channel3: Channel<Int>, channel4: Channel<Int>) {
    val received = channel3.receive()
    channel4.send(received + 1)
    println("coroutine3 received and sent: ${received + 1}")
}

suspend fun coroutine4(channel4: Channel<Int>, channel1: Channel<Int>) {
    val received = channel4.receive()
    channel1.send(received + 1)
    println("coroutine4 received and sent: ${received + 1}")
}

suspend fun coroutine5(channel1: Channel<Int>) {
    val received = channel1.receive()
    println("coroutine5 received: $received")
}

suspend fun coroutine6(channel2: Channel<Int>) {
    val received = channel2.receive()
    println("coroutine6 received: $received")
}

suspend fun coroutine7(channel3: Channel<Int>) {
    val received = channel3.receive()
    println("coroutine7 received: $received")
}

class RunChecker68: RunCheckerBase() {
    override fun block() = main()
}