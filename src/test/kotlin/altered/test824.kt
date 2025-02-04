/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":4,"nChannels":3,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
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
package org.example.altered.test824
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun main(): Unit= runBlocking {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()
    val channelC = Channel<Int>()

    launch { function1(channelA, channelB) }
    launch { function2(channelB, channelC) }
    launch { function3(channelC, channelA) }
    launch { function4(channelA, channelB, channelC) }

    delay(1000L) // Give the coroutines some time to run
}

suspend fun function1(channelA: Channel<Int>, channelB: Channel<Int>) {
    coroutineScope {
        launch {
            channelA.send(1)
            channelB.receive()
        }
    }
}

suspend fun function2(channelB: Channel<Int>, channelC: Channel<Int>) {
    coroutineScope {
        launch {
            channelB.send(1)
            channelC.receive()
        }
    }
}

suspend fun function3(channelC: Channel<Int>, channelA: Channel<Int>) {
    coroutineScope {
        launch {
            channelC.send(1)
            channelA.receive()
        }
    }
}

suspend fun function4(channelA: Channel<Int>, channelB: Channel<Int>, channelC: Channel<Int>) {
    coroutineScope {
        launch {
            channelA.receive()
            channelB.receive()
            channelC.receive()
        }
    }
}

class RunChecker824: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}