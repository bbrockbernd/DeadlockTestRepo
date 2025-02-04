/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":5,"nChannels":5,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 5 different channels
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
package org.example.altered.test800
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun foo(channel1: Channel<Int>) {
    runBlocking {
        launch {
            for (i in 1..5) {
                channel1.send(i)
            }
            channel1.close()
        }
    }
}

fun bar(channel1: Channel<Int>, channel2: Channel<Int>) {
    runBlocking {
        launch {
            for (value in channel1) {
                channel2.send(value * 2)
            }
            channel2.close()
        }
    }
}

fun baz(channel2: Channel<Int>, channel3: Channel<Int>) {
    runBlocking {
        launch {
            for (value in channel2) {
                channel3.send(value + 1)
            }
            channel3.close()
        }
    }
}

fun qux(channel3: Channel<Int>, channel4: Channel<Int>) {
    runBlocking {
        launch {
            for (value in channel3) {
                channel4.send(value - 1)
            }
            channel4.close()
        }
    }
}

fun quux(channel4: Channel<Int>) {
    runBlocking {
        launch {
            for (value in channel4) {
                println("Received $value")
            }
        }
    }
}

fun main(): Unit{
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()

    foo(channel1)
    bar(channel1, channel2)
    baz(channel2, channel3)
    qux(channel3, channel4)
    quux(channel4)
}

class RunChecker800: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}