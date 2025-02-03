/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":5,"nChannels":3,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 3 different channels
- 5 different coroutines
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
package org.example.altered.test466
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun firstFunction(channel1: Channel<Int>, channel2: Channel<Int>) = runBlocking {
    launch {
        for (i in 1..5) {
            val result = i * i
            channel1.send(result)
        }
        channel1.close()
    }

    launch {
        for (value in channel1) {
            val result = value + 1
            channel2.send(result)
        }
        channel2.close()
    }
}

fun secondFunction(channel2: Channel<Int>, channel3: Channel<Int>) = runBlocking {
    launch {
        for (value in channel2) {
            val result = value * 2
            channel3.send(result)
        }
        channel3.close()
    }

    launch {
        for (value in channel3) {
            println("Final result: $value")
        }
    }
}

fun main(): Unit = runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()

    launch { firstFunction(channel1, channel2) }
    launch { secondFunction(channel2, channel3) }

    delay(2000L)  // Allow some time for the coroutines to complete
}

class RunChecker466: RunCheckerBase() {
    override fun block() = main()
}