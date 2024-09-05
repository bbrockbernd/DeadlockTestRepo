/* 
{"deadlock":true,"nFunctions":1,"nCoroutines":5,"nChannels":1,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
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
package org.example.generated.test738
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class TestClass {
    val channel = Channel<Int>()

    suspend fun sendToChannel(value: Int) {
        channel.send(value)
    }

    suspend fun receiveFromChannel(): Int {
        return channel.receive()
    }
}

fun launchCoroutineOne(tc: TestClass) = GlobalScope.launch {
    tc.sendToChannel(1)
    println("Sent 1 to the channel")
    println("Received from the channel: ${tc.receiveFromChannel()}")
}

fun launchCoroutineTwo(tc: TestClass) = GlobalScope.launch {
    println("Received from the channel: ${tc.receiveFromChannel()}")
    tc.sendToChannel(2)
    println("Sent 2 to the channel")
}

fun launchCoroutineThree(tc: TestClass) = GlobalScope.launch {
    tc.sendToChannel(3)
    println("Sent 3 to the channel")
    println("Received from the channel: ${tc.receiveFromChannel()}")
}

fun launchCoroutineFour(tc: TestClass) = GlobalScope.launch {
    println("Received from the channel: ${tc.receiveFromChannel()}")
    tc.sendToChannel(4)
    println("Sent 4 to the channel")
}

fun launchCoroutineFive(tc: TestClass) = GlobalScope.launch {
    tc.sendToChannel(5)
    println("Sent 5 to the channel")
    println("Received from the channel: ${tc.receiveFromChannel()}")
}

fun main(): Unit= runBlocking {
    val tc = TestClass()
    launchCoroutineOne(tc)
    launchCoroutineTwo(tc)
    launchCoroutineThree(tc)
    launchCoroutineFour(tc)
    launchCoroutineFive(tc)

    delay(5000)
}