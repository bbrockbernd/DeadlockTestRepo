/* 
{"deadlock":true,"nFunctions":7,"nCoroutines":6,"nChannels":3,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 3 different channels
- 6 different coroutines
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
package org.example.generated.test400
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

fun function1(channel1: Channel<Int>, channel2: Channel<Int>) {
    runBlocking {
        launch {
            repeat(5) {
                val receivedValue = channel1.receive()
                channel2.send(receivedValue + 1)
            }
        }
    }
}

fun function2(channel2: Channel<Int>, channel3: Channel<Int>) {
    runBlocking {
        launch {
            repeat(5) {
                val receivedValue = channel2.receive()
                channel3.send(receivedValue + 1)
            }
        }
    }
}

fun function3(channel3: Channel<Int>, channel1: Channel<Int>) {
    runBlocking {
        launch {
            repeat(5) {
                val receivedValue = channel3.receive()
                channel1.send(receivedValue + 1)
            }
        }
    }
}

fun function4(channel1: Channel<Int>) {
    runBlocking {
        launch {
            repeat(5) {
                channel1.send(it)
            }
        }
    }
}

fun function5(channel2: Channel<Int>) {
    runBlocking {
        launch {
            repeat(5) {
                channel2.send(it)
            }
        }
    }
}

fun function6(channel3: Channel<Int>) {
    runBlocking {
        launch {
            repeat(5) {
                channel3.send(it)
            }
        }
    }
}

fun function7(channel1: Channel<Int>, channel2: Channel<Int>, channel3: Channel<Int>) {
    runBlocking {
        launch { function1(channel1, channel2) }
        launch { function2(channel2, channel3) }
        launch { function3(channel3, channel1) }
        launch { function4(channel1) }
        launch { function5(channel2) }
        launch { function6(channel3) }
    }
}

fun main(): Unit{
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()

    function7(channel1, channel2, channel3)
}