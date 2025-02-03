/* 
{"deadlock":true,"nFunctions":2,"nCoroutines":2,"nChannels":1,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
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
package org.example.altered.test576
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

fun producer(channel: Channel<Int>) {
    runBlocking {
        launch {
            channel.send(1)
            delay(1000)
            channel.send(2)
        }
    }
}

fun consumer(channel: Channel<Int>) {
    runBlocking {
        launch {
            delay(1000)
            println(channel.receive())
            println(channel.receive())
        }
    }
}

fun main(): Unit{
    val channel = Channel<Int>()
    producer(channel)
    consumer(channel)
}

class RunChecker576: RunCheckerBase() {
    override fun block() = main()
}