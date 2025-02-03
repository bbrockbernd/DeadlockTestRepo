/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":4,"nChannels":1,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
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
package org.example.altered.test515
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun functionOne(channel: Channel<Int>) {
    GlobalScope.launch {
        repeat(10) { 
            delay(100L)
            channel.send(it * it)
        }
        channel.close()
    }
}

fun functionTwo(channel: Channel<Int>) {
    GlobalScope.launch {
        for (received in channel) {
            println("Received: $received")
        }
    }
}

suspend fun functionThree(channel: Channel<Int>) {
    coroutineScope {
        launch {
            delay(50L)
            for (i in 1..5) {
                val square = i * i
                println("Squaring: $square")
            }
        }
    }
}

fun main(): Unit= runBlocking {
    val channel = Channel<Int>()
    launch { functionOne(channel) }
    launch { functionTwo(channel) }
    launch { functionThree(channel) }
    coroutineScope {
        launch {
            delay(700L)
            println("Finished work")
        }
    }
}

class RunChecker515: RunCheckerBase() {
    override fun block() = main()
}