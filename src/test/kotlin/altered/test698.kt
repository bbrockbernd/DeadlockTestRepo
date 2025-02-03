/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":3,"nChannels":3,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 3 different channels
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
package org.example.altered.test698
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class MyChannelProcessor {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>(5)
    val channel3 = Channel<String>()

    suspend fun processFirst() {
        coroutineScope {
            launch {
                channel1.send(5)
            }
            launch {
                val received = channel1.receive()
                channel2.send(received * 2)
            }
        }
    }

    suspend fun processSecond() {
        coroutineScope {
            launch {
                val received = channel2.receive()
                channel3.send("Result: $received")
            }
        }
    }

    suspend fun processThird() {
        coroutineScope {
            launch {
                val result = channel3.receive()
                println(result)
            }
        }
    }
}

fun main(): Unit= runBlocking {
    val processor = MyChannelProcessor()
    processor.processFirst()
    processor.processSecond()
    processor.processThird()
}

class RunChecker698: RunCheckerBase() {
    override fun block() = main()
}