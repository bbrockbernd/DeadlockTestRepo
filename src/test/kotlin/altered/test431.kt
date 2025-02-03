/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":1,"nChannels":2,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 2 different channels
- 1 different coroutines
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
package org.example.altered.test431
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

val firstChannel = Channel<Int>()
val secondChannel = Channel<Int>()

fun producer() = GlobalScope.launch {
    for (i in 1..5) {
        firstChannel.send(i)
    }
    firstChannel.close()
}

fun consumer() = GlobalScope.launch {
    for (value in firstChannel) {
        process(value)
    }
    secondChannel.close()
}

fun process(value: Int) = GlobalScope.launch {
    val result = value * 2
    secondChannel.send(result)
}

suspend fun displayResults() {
    for (result in secondChannel) {
        println("Result: $result")
    }
}

fun main(): Unit = runBlocking {
    producer()
    consumer()
    displayResults()
}

class RunChecker431: RunCheckerBase() {
    override fun block() = main()
}