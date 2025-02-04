/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":5,"nChannels":1,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 1 different channels
- 5 different coroutines
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
package org.example.altered.test672
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ChannelDeadlockExample {
    private val channel = Channel<Int>()

    suspend fun functionOne() {
        channel.send(1)
        println("Sent: 1 from functionOne")
    }

    suspend fun functionTwo() {
        val received = channel.receive()
        println("Received: $received in functionTwo")
        channel.send(received + 1)
        println("Sent: ${received + 1} from functionTwo")
    }

    suspend fun functionThree() {
        val received = channel.receive()
        println("Received: $received in functionThree")
    }

    fun runExample() = runBlocking {
        launch {
            functionOne()
        }

        launch {
            functionTwo()
        }

        launch {
            functionThree()
        }

        launch {
            functionOne()
        }

        launch {
            functionTwo()
        }
    }
}

fun main(): Unit{
    val example = ChannelDeadlockExample()
    example.runExample()
}

class RunChecker672: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}