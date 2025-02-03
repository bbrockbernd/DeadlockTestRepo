/* 
{"deadlock":true,"nFunctions":2,"nCoroutines":5,"nChannels":1,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 1 different channels
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
package org.example.altered.test992
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

suspend fun producer(channel: Channel<Int>) {
    coroutineScope {
        launch {
            channel.send(1)
            channel.send(2)
        }
    }
}

suspend fun consumer(channel: Channel<Int>) {
    coroutineScope {
        launch {
            println(channel.receive())
        }
        launch {
            println(channel.receive())
        }
        launch {
            // Intentional extra receive call to create deadlock
            println(channel.receive())
        }
    }
}

fun main(): Unit= runBlocking {
    val channel = Channel<Int>()
    launch {
        producer(channel)
    }
    launch {
        consumer(channel)
    }
}

class RunChecker992: RunCheckerBase() {
    override fun block() = main()
}