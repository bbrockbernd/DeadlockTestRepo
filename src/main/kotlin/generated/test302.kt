/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":5,"nChannels":5,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 5 different channels
- 5 different coroutines
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
package org.example.generated.test302
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Processor(val name: String) {
    suspend fun process(input: Channel<Int>, output: Channel<Int>) {
        for (item in input) {
            output.send(item * 2)
        }
        output.close()
    }
}

class Aggregator(val name: String) {
    suspend fun aggregate(input1: Channel<Int>, input2: Channel<Int>, output: Channel<Int>) {
        for (item1 in input1) {
            for (item2 in input2) {
                output.send(item1 + item2)
            }
        }
        output.close()
    }
}

fun createChannels(): List<Channel<Int>> {
    return listOf(Channel(), Channel(), Channel(), Channel(), Channel())
}

fun main(): Unit{
    runBlocking {
        val channels = createChannels()
        val processor = Processor("Processor1")
        val aggregator = Aggregator("Aggregator1")

        launch {
            processor.process(channels[0], channels[1])
        }
        launch {
            processor.process(channels[1], channels[2])
        }
        launch {
            aggregator.aggregate(channels[2], channels[3], channels[4])
        }
        launch {
            channels[0].send(1)
            channels[0].close()
        }
        launch {
            println("Result: ${channels[4].receive()}")
        }
    }
}