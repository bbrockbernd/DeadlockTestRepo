/* 
{"deadlock":true,"nFunctions":2,"nCoroutines":5,"nChannels":1,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 1 different channels
- 5 different coroutines
- 1 different classes

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
package org.example.generated.test984
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Processor(val channel: Channel<Int>) {
    suspend fun produceData() {
        channel.send(1)
        channel.send(2)
        channel.send(3)
    }

    suspend fun consumeData() {
        repeat(3) {
            println("Consumed: ${channel.receive()}")
        }
    }
}

fun functionOne(channel: Channel<Int>) {
    val processor = Processor(channel)
    runBlocking {
        coroutineScope {
            launch { processor.produceData() }
            launch { processor.consumeData() }
        }
    }
}

fun functionTwo(channel: Channel<Int>) {
    runBlocking {
        coroutineScope {
            launch { channel.send(4) }
            launch { println("Received: ${channel.receive()}") }
        }
    }
}

fun main(): Unit{
    val channel = Channel<Int>()

    runBlocking {
        launch { functionOne(channel) }
        launch { functionTwo(channel) }
        launch { channel.send(5) }
        launch { println("Received in main: ${channel.receive()}") }
        launch { println("Received in main: ${channel.receive()}") }
    }
}