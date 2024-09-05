/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":1,"nChannels":4,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 4 different channels
- 1 different coroutines
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
package org.example.generated.test564
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class DeadlockDetector {
    private val channel1 = Channel<Int>()
    private val channel2 = Channel<Int>()
    private val channel3 = Channel<Int>()
    private val channel4 = Channel<Int>()

    fun start() = runBlocking {
        launch {
            function1()
        }
        coroutineScope {
            launch {
                function2()
            }
            function3()
        }
    }

    private suspend fun function1() {
        channel1.send(1)
        channel3.send(channel1.receive())
        channel2.send(channel3.receive())
    }

    private suspend fun function2() {
        channel3.send(2)
        channel4.receive()
    }

    private suspend fun function3() {
        channel4.send(channel2.receive())
        channel1.receive()
    }
}

fun main(): Unit{
    DeadlockDetector().start()
}