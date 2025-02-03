/* 
{"deadlock":true,"nFunctions":7,"nCoroutines":2,"nChannels":1,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 1 different channels
- 2 different coroutines
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
package org.example.altered.test200
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun main(): Unit= runBlocking {
    val channel = Channel<Int>()

    launch {
        firstFunction(channel)
    }

    launch {
        thirdFunction(channel)
    }
}

suspend fun firstFunction(channel: Channel<Int>) {
    val result = secondFunction()
    channel.send(result)
}

fun secondFunction(): Int {
    return 42
}

suspend fun thirdFunction(channel: Channel<Int>) {
    val result = fourthFunction(channel)
    fifthFunction(result)
}

suspend fun fourthFunction(channel: Channel<Int>): Int {
    return channel.receive()
}

fun fifthFunction(value: Int) {
    println("Received value: $value")
}

suspend fun coroutineOne(channel: Channel<Int>) {
    val result = sixthFunction(channel)
    channel.send(result)
}

suspend fun sixthFunction(channel: Channel<Int>): Int {
    return channel.receive()
}

suspend fun seventhFunction(channel: Channel<Int>) {
    val result = sixthFunction(channel)
    fifthFunction(result)
}

class RunChecker200: RunCheckerBase() {
    override fun block() = main()
}