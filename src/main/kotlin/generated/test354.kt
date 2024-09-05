/* 
{"deadlock":true,"nFunctions":8,"nCoroutines":4,"nChannels":5,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
- 5 different channels
- 4 different coroutines
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
package org.example.generated.test354
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class ChannelTest {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>(10)
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>(5)

    fun functionA() = runBlocking {
        launch { coroutineA() }
        launch { coroutineB() }
        launch { coroutineC() }
        launch { coroutineD() }
    }

    suspend fun coroutineA() {
        val value = channel1.receive()
        channel2.send(value + 1)
    }

    suspend fun coroutineB() {
        val value = channel2.receive()
        channel3.send(value + 2)
    }

    suspend fun coroutineC() {
        val value = channel3.receive()
        channel4.send(value + 3)
    }

    suspend fun coroutineD() {
        val value = channel4.receive()
        channel5.send(value + 4)
    }

    fun functionB() = runBlocking {
        channel1.send(1)
        functionC()
    }

    fun functionC() {
        functionD()
    }

    fun functionD() {
        runBlocking {
            channel5.receive()
        }
    }
}

fun functionE() {
    val test = ChannelTest()
    test.functionA()
    test.functionB()
}

fun main(): Unit{
    functionE()
}