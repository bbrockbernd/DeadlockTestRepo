/* 
{"deadlock":false,"nFunctions":8,"nCoroutines":5,"nChannels":2,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
- 2 different channels
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
package org.example.altered.test320
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

// Function 1
fun createChannel1(): Channel<Int> {
    return Channel()
}

// Function 2
fun createChannel2(): Channel<String> {
    return Channel()
}

// Function 3
suspend fun producer1(channel: Channel<Int>) {
    for (i in 1..5) {
        channel.send(i)
    }
    channel.close()
}

// Function 4
suspend fun producer2(channel: Channel<String>) {
    val messages = listOf("A", "B", "C", "D", "E")
    for (message in messages) {
        channel.send(message)
    }
    channel.close()
}

// Function 5
suspend fun consumer1(channel: Channel<Int>) {
    for (x in channel) {
        println("Consumer 1 received: $x")
    }
}

// Function 6
suspend fun consumer2(channel: Channel<String>) {
    for (x in channel) {
        println("Consumer 2 received: $x")
    }
}

// Function 7
fun setupCoroutines(channel1: Channel<Int>, channel2: Channel<String>) {
    runBlocking {
        launch { producer1(channel1) }
        launch { producer2(channel2) }
        launch { consumer1(channel1) }
        launch { consumer2(channel2) }
    }
}

// Function 8
fun main(): Unit{
    val channel1 = createChannel1()
    val channel2 = createChannel2()
    setupCoroutines(channel1, channel2)
}

class RunChecker320: RunCheckerBase() {
    override fun block() = main()
}