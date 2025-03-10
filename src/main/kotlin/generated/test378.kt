/* 
{"deadlock":true,"nFunctions":6,"nCoroutines":6,"nChannels":8,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 8 different channels
- 6 different coroutines
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
package org.example.generated.test378
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class DeadlockExample {
    private val channel1 = Channel<Int>()
    private val channel2 = Channel<Int>()
    private val channel3 = Channel<Int>()
    private val channel4 = Channel<Int>()
    private val channel5 = Channel<Int>()
    private val channel6 = Channel<Int>()
    private val channel7 = Channel<Int>()
    private val channel8 = Channel<Int>()

    fun functionA() = runBlocking {
        launch { coroutineA() }
        launch { coroutineB() }
    }

    suspend fun functionB() {
        channel1.send(1)
        channel2.send(2)
    }

    suspend fun functionC() {
        channel3.send(3)
        channel4.send(4)
    }

    suspend fun functionD() {
        coroutineScope {
            launch {
                val value1 = channel5.receive()
                channel6.send(value1)
            }
            launch {
                val value2 = channel6.receive()
                channel7.send(value2)
            }
        }
    }

    fun coroutineA() = runBlocking {
        launch {
            channel3.send(10)
            channel1.receive()
        }
        launch {
            channel4.send(20)
            channel2.receive()
        }
    }

    fun coroutineB() = runBlocking {
        launch {
            channel5.send(30)
            channel7.receive()
        }
        launch {
            functionE()
        }
    }

    suspend fun functionE() {
        val value3 = channel3.receive()
        val value4 = channel4.receive()
        channel8.send(value3 + value4)
    }
}

fun main(): Unit{
    val example = DeadlockExample()
    example.functionA()
    runBlocking {
        example.functionB()
        example.functionC()
        example.functionD()
    }
}