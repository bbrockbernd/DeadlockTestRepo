/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":3,"nChannels":4,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 4 different channels
- 3 different coroutines
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
package org.example.generated.test713
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer {
    suspend fun produce(output: Channel<Int>) {
        for (i in 1..3) {
            output.send(i)
        }
        output.close()
    }
}

class Consumer {
    suspend fun consume(input: Channel<Int>, output: Channel<Int>) {
        for (value in input) {
            output.send(value * 2)
        }
        output.close()
    }

    suspend fun relay(input: Channel<Int>, output: Channel<Int>) {
        for (value in input) {
            output.send(value + 1)
        }
        output.close()
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()

    launch {
        Producer().produce(channel1)
    }

    launch {
        Consumer().consume(channel1, channel2)
    }

    launch {
        Consumer().relay(channel2, channel3)
    }

    coroutineScope {
        for (value in channel3) {
            channel4.send(value - 1)
        }
        channel4.close()
    }

    for (value in channel4) {
        println(value)
    }
}