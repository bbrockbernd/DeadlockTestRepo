/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":4,"nChannels":1,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 1 different channels
- 4 different coroutines
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
package org.example.altered.test885
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class SampleController {
    private val channel = Channel<Int>()

    suspend fun producer() {
        var value = 0
        repeat(5) {
            channel.send(value)
            value += 1
        }
        channel.close()
    }

    suspend fun consumer() {
        for (item in channel) {
            println("Received: $item")
        }
    }
}

fun main(): Unit= runBlocking {
    val controller = SampleController()

    launch {
        controller.producer()
    }

    launch {
        controller.consumer()
    }

    launch {
        delay(500)
        controller.producer()
    }

    launch {
        delay(700)
        controller.consumer()
    }
}

class RunChecker885: RunCheckerBase() {
    override fun block() = main()
}