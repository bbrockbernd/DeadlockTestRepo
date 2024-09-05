/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":3,"nChannels":7,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 7 different channels
- 3 different coroutines
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
package org.example.generated.test168
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun function1(channel1: Channel<Int>, channel2: Channel<Int>, channel3: Channel<Int>) {
    runBlocking {
        launch {
            repeat(5) {
                channel1.send(it * 2)
            }
            channel1.close()
        }

        launch {
            for (value in channel1) {
                channel2.send(value + 1)
            }
            channel2.close()
        }

        launch {
            for (value in channel2) {
                channel3.send(value * 3)
            }
            channel3.close()
        }
    }
}

fun function2(channel4: Channel<Int>, channel5: Channel<Int>) {
    runBlocking {
        launch {
            for (value in channel4) {
                channel5.send(value * 2)
            }
            channel5.close()
        }
    }
}

fun function3(channel6: Channel<Int>, channel7: Channel<Int>) {
    runBlocking {
        launch {
            channel6.send(1)
            channel6.send(2)
            channel6.send(3)
            channel6.close()
        }

        launch {
            for (value in channel6) {
                channel7.send(value * 10)
            }
            channel7.close()
        }
    }
}

fun function4(channel3: Channel<Int>, channel5: Channel<Int>, channel7: Channel<Int>) {
    runBlocking {
        launch {
            for (value in channel3) {
                println("From channel3: $value")
            }
        }

        launch {
            for (value in channel5) {
                println("From channel5: $value")
            }
        }

        launch {
            for (value in channel7) {
                println("From channel7: $value")
            }
        }
    }
}

fun main(): Unit{
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()
    val channel6 = Channel<Int>()
    val channel7 = Channel<Int>()

    function1(channel1, channel2, channel3)
    function2(channel4, channel5)
    function3(channel6, channel7)
    function4(channel3, channel5, channel7)
}