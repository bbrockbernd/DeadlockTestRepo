/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":2,"nChannels":3,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 3 different channels
- 2 different coroutines
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
package org.example.altered.test995
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class DemoClass(private val channel1: Channel<Int>, private val channel2: Channel<Int>, private val channel3: Channel<Int>) {
    suspend fun functionOne() {
        channel1.send(1)
        val received = channel2.receive()
        channel3.send(received)
    }

    suspend fun functionTwo() {
        val received = channel1.receive()
        channel2.send(received)
    }

    suspend fun functionThree(): Int {
        return channel3.receive()
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val demoClass = DemoClass(channel1, channel2, channel3)

    launch {
        demoClass.functionOne()
    }

    launch {
        demoClass.functionTwo()
    }

    val result = demoClass.functionThree()
    println("Result: $result")
}

class RunChecker995: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}