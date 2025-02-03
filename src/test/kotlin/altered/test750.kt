/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":3,"nChannels":5,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 5 different channels
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
package org.example.altered.test750
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ChannelManager {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<String>()
    val channel5 = Channel<String>()

    suspend fun processIntegers() {
        launch {
            val result1 = channel1.receive() + 1
            channel2.send(result1)
        }
        launch {
            val result2 = channel2.receive() + 1
            channel3.send(result2)
        }
        launch {
            val result3 = channel3.receive() + 1
            println("Final integer result: $result3")
        }
    }

    suspend fun processStrings() {
        launch {
            val result1 = channel4.receive() + " World"
            channel5.send(result1)
        }
        launch {
            val result2 = channel5.receive() + "!"
            println("Final string result: $result2")
        }
    }
}

fun main(): Unit= runBlocking {
    val manager = ChannelManager()

    launch {
        manager.channel1.send(1)
        manager.processIntegers()
    }

    launch {
        manager.channel4.send("Hello")
        manager.processStrings()
    }
}

class RunChecker750: RunCheckerBase() {
    override fun block() = main()
}