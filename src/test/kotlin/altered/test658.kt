/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":1,"nChannels":1,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 1 different channels
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
package org.example.altered.test658
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun produce(channel: Channel<Int>) {
    runBlocking {
        repeat(5) {
            channel.send(it)
        }
        channel.close()
    }
}

suspend fun consume(channel: Channel<Int>) {
    for (value in channel) {
        processValue(value)
    }
}

suspend fun processValue(value: Int) {
    printValue(value)
}

fun printValue(value: Int) {
    println(value)
}

fun main(): Unit{
    val channel = Channel<Int>()

    runBlocking {
        launch { produce(channel) }
        launch { consume(channel) }
    }
}

class RunChecker658: RunCheckerBase() {
    override fun block() = main()
}