/* 
{"deadlock":true,"nFunctions":8,"nCoroutines":7,"nChannels":3,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
- 3 different channels
- 7 different coroutines
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
package org.example.generated.test454
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun function1(channel1: Channel<Int>, channel2: Channel<Int>) = runBlocking {
    launch { channel1.send(1) }
    launch { channel2.send(channel1.receive()) }
}

fun function2(channel2: Channel<Int>, channel3: Channel<Int>) = runBlocking {
    launch { channel3.send(channel2.receive()) }
    launch { channel2.send(2) }
}

fun function3(channel1: Channel<Int>, channel3: Channel<Int>) = runBlocking {
    launch { 
        val value = channel3.receive()
        channel1.send(value + 1) 
    }
}

fun function4(channel1: Channel<Int>) = runBlocking {
    launch { 
        val value = channel1.receive()
        channel1.send(value * 2) 
    }
}

fun function5(channel3: Channel<Int>) = runBlocking {
    launch { channel3.send(3) }
}

fun function6(channel2: Channel<Int>) = runBlocking {
    launch {
        val value = channel2.receive()
        println("Received from channel2: $value")
    }
}

fun function7(channel3: Channel<Int>) = runBlocking {
    launch { 
        val value = channel3.receive()
        channel3.send(value + 1)
    }
}

fun function8(channel1: Channel<Int>, channel3: Channel<Int>) = runBlocking {
    launch { 
        val value1 = channel1.receive()
        val value3 = channel3.receive()
        println("Received from channels: $value1, $value3")
    }
}

fun main(): Unit{
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()

    runBlocking {
        launch { function1(channel1, channel2) }
        launch { function2(channel2, channel3) }
        launch { function3(channel1, channel3) }
        launch { function4(channel1) }
        launch { function5(channel3) }
        launch { function6(channel2) }
        launch { function7(channel3) }
        launch { function8(channel1, channel3) }
    }
}