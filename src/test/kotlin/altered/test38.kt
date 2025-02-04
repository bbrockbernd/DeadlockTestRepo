/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":4,"nChannels":3,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 3 different channels
- 4 different coroutines
- 5 different classes

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
package org.example.altered.test38
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA(val channel1: Channel<Int>, val channel2: Channel<Int>) {
    suspend fun funA() {
        val value = channel2.receive()
        channel1.send(value + 1)
    }
}

class ClassB(val channel2: Channel<Int>, val channel3: Channel<Int>) {
    suspend fun funB() {
        val value = channel3.receive()
        channel2.send(value * 2)
    }
}

class ClassC(val channel3: Channel<Int>, val channel1: Channel<Int>) {
    suspend fun funC() {
        val value = channel1.receive()
        channel3.send(value - 1)
    }
}

suspend fun functionA(classA: ClassA) {
    classA.funA()
}

suspend fun functionB(classB: ClassB) {
    classB.funB()
}

suspend fun functionC(classC: ClassC) {
    classC.funC()
}

fun main(): Unit{
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val classA = ClassA(channel1, channel2)
    val classB = ClassB(channel2, channel3)
    val classC = ClassC(channel3, channel1)

    runBlocking {
        launch { functionA(classA) }
        launch { functionB(classB) }
        launch { functionC(classC) }
        launch { channel1.send(1) }
    }
}

class RunChecker38: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}