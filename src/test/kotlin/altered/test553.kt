/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":4,"nChannels":4,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 4 different channels
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
package org.example.altered.test553
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun startCoroutine1(channel1: Channel<Int>, channel2: Channel<Int>) {
    GlobalScope.launch {
        for (i in 1..10) {
            channel1.send(i)
        }
        channel1.close()
    }

    GlobalScope.launch {
        for (i in channel1) {
            channel2.send(i * 2)
        }
        channel2.close()
    }
}

fun startCoroutine2(channel3: Channel<Int>, channel4: Channel<Int>) {
    GlobalScope.launch {
        for (i in 11..20) {
            channel3.send(i)
        }
        channel3.close()
    }

    GlobalScope.launch {
        for (i in channel3) {
            channel4.send(i * 2)
        }
        channel4.close()
    }
}

fun consumeChannels(channel: Channel<Int>, name: String) {
    GlobalScope.launch {
        for (i in channel) {
            println("$name received: $i")
        }
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()

    startCoroutine1(channel1, channel2)
    startCoroutine2(channel3, channel4)

    consumeChannels(channel2, "Channel2")
    consumeChannels(channel4, "Channel4")

    delay(2000) // to keep the main coroutine alive
}

class RunChecker553: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}