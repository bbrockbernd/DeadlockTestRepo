/* 
{"deadlock":false,"nFunctions":1,"nCoroutines":4,"nChannels":4,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
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
package org.example.altered.test600
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

suspend fun coroutineTask(channelA: Channel<Int>, channelB: Channel<Int>, channelC: Channel<Int>, channelD: Channel<Int>) {
    coroutineScope {
        launch {
            for (i in 1..5) {
                channelA.send(i)
            }
            channelA.close()
        }
        launch {
            for (x in channelA) {
                channelB.send(x * 2)
            }
            channelB.close()
        }
        launch {
            for (y in channelB) {
                channelC.send(y + 1)
            }
            channelC.close()
        }
        launch {
            for (z in channelC) {
                channelD.send(z * z)
            }
            channelD.close()
        }
    }
}

fun main(): Unit= runBlocking {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()
    val channelC = Channel<Int>()
    val channelD = Channel<Int>()

    launch {
        coroutineTask(channelA, channelB, channelC, channelD)
    }

    launch {
        for (result in channelD) {
            println("Final Result: $result")
        }
    }
}

class RunChecker600: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}