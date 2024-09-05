/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":4,"nChannels":1,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 1 different channels
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
package org.example.generated.test619
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun main(): Unit= runBlocking {
    val channel = Channel<Int>()

    launch { firstCoroutine(channel) }
    launch { secondCoroutine(channel) }
    launch { thirdCoroutine(channel) }
    launch { fourthCoroutine(channel) }
}

suspend fun firstCoroutine(channel: Channel<Int>) {
    channel.send(1)
}

suspend fun secondCoroutine(channel: Channel<Int>) {
    val value = channel.receive()
    fourthFunction(value)
}

suspend fun thirdCoroutine(channel: Channel<Int>) {
    thirdFunction()
    val value = firstFunction()
    channel.send(value)
}

suspend fun fourthCoroutine(channel: Channel<Int>) {
    val value = channel.receive()
    secondFunction(value)
}

fun firstFunction(): Int {
    return 2
}

fun secondFunction(input: Int) {
    println("Second Function received: $input")
}

fun thirdFunction() {
    println("Third Function called")
}

fun fourthFunction(input: Int) {
    println("Fourth Function received: $input")
}