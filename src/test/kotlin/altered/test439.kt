/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":3,"nChannels":1,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 1 different channels
- 3 different coroutines
- 1 different classes

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
package org.example.altered.test439
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Processor {
    private val channel = Channel<Int>()

    fun startProcessing() {
        runBlocking {
            launch { producer() }
            launch { consumer() }
            launch { intermediary() }
        }
    }

    private suspend fun producer() {
        repeat(5) {
            channel.send(it)
        }
        channel.close()
    }

    private suspend fun consumer() {
        for (value in channel) {
            println("Consumed: $value")
        }
    }

    private suspend fun intermediary() {
        coroutineScope {
            launch {
                for (value in channel) {
                    channel.send(value * 2)
                }
            }
        }
    }
}

fun main(): Unit{
    Processor().startProcessing()
}

class RunChecker439: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}