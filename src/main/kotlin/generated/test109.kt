/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":1,"nChannels":5,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 5 different channels
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
package org.example.generated.test109
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

val channel1 = Channel<Int>()
val channel2 = Channel<Int>()
val channel3 = Channel<Int>()
val channel4 = Channel<Int>()
val channel5 = Channel<Int>()

fun function1() {
    runBlocking {
        launch {
            val data = channel1.receive()
            channel2.send(data + 1)
        }
    }
}

fun function2() {
    runBlocking {
        launch {
            val data = channel3.receive()
            channel4.send(data + 1)
        }
    }
}

fun function3() {
    runBlocking {
        launch {
            val data = channel2.receive()
            channel5.send(data + 1)
        }
    }
}

fun function4() {
    runBlocking {
        launch {
            val data = channel4.receive()
            channel1.send(data + 1)
        }
    }
}

fun main(): Unit{
    runBlocking {
        val job = launch {
            function1()
            function2()
            function3()
            function4()
            launch {
                channel3.send(1)
                val result = channel5.receive()
                println("Received: $result")
            }
        }
        job.join()
    }
}