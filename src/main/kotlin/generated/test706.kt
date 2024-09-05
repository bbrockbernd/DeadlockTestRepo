/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":2,"nChannels":1,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 1 different channels
- 2 different coroutines
- 2 different classes

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
package org.example.generated.test706
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer {
    suspend fun produce(channel: Channel<Int>) {
        for (i in 1..5) {
            delay(100)
            channel.send(i)
        }
        channel.close()
    }
}

class Consumer {
    suspend fun consume(channel: Channel<Int>) {
        for (value in channel) {
            processValue(value)
        }
    }

    private fun processValue(value: Int) {
        println("Consumed: $value")
    }
}

fun main(): Unit= runBlocking {
    val channel = Channel<Int>()

    launch {
        Producer().produce(channel)
    }

    launch {
        Consumer().consume(channel)
    }
}

fun helperFunction() {
    println("This is a helper function.")
}