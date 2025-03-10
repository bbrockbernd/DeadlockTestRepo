/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":1,"nChannels":5,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 5 different channels
- 1 different coroutines
- 3 different classes

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
package org.example.generated.test826
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA(val channel1: Channel<Int>, val channel2: Channel<Int>) {
    suspend fun functionA() {
        val value = channel1.receive()
        channel2.send(value)
    }
}

class ClassB(val channel2: Channel<Int>, val channel3: Channel<Int>) {
    suspend fun functionB() {
        val value = channel2.receive()
        channel3.send(value)
    }
}

class ClassC(val channel3: Channel<Int>, val channel4: Channel<Int>) {
    suspend fun functionC() {
        val value = channel3.receive()
        channel4.send(value)
    }
}

fun functionD(channel4: Channel<Int>, channel5: Channel<Int>) = runBlocking {
    launch {
        val value = channel4.receive()
        channel5.send(value)
    }
}

fun testDeadlock() = runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()

    val classA = ClassA(channel1, channel2)
    val classB = ClassB(channel2, channel3)
    val classC = ClassC(channel3, channel4)

    launch { classA.functionA() }
    launch { classB.functionB() }
    launch { classC.functionC() }
    functionD(channel4, channel5)

    // Initiate deadlock
    channel1.send(1)
}

fun main(): Unit{
    testDeadlock()
}