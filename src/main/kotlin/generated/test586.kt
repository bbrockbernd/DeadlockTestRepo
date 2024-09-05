/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":2,"nChannels":5,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 5 different channels
- 2 different coroutines
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
package org.example.generated.test586
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class DeadlockDetector {
    private val channel1 = Channel<Int>()
    private val channel2 = Channel<Int>()
    private val channel3 = Channel<Int>()
    private val channel4 = Channel<Int>()
    private val channel5 = Channel<Int>()

    suspend fun function1() {
        val value = channel1.receive()
        channel2.send(value)
    }

    suspend fun function2() {
        val value = channel2.receive()
        channel3.send(value)
    }

    suspend fun function3() {
        val value = channel3.receive()
        channel4.send(value)
    }

    suspend fun function4() {
        val value = channel4.receive()
        channel5.send(value)
    }

    suspend fun function5() {
        val value = channel5.receive()
        channel1.send(value)
    }

    fun startCoroutines() {
        runBlocking {
            launch {
                function1()
                function2()
            }
            launch {
                function3()
                function4()
                function5()
            }
        }
    }
}

fun main(): Unit{
    val detector = DeadlockDetector()
    detector.startCoroutines()
}