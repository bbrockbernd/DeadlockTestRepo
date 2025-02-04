/* 
{"deadlock":true,"nFunctions":7,"nCoroutines":1,"nChannels":4,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 4 different channels
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
package org.example.altered.test321
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA(val channel1: Channel<Int>, val channel2: Channel<Int>) {
    suspend fun functionA() {
        channel1.send(1)
        val result = channel2.receive()
        println("ClassA received: $result")
    }
}

class ClassB(val channel1: Channel<Int>, val channel3: Channel<Int>) {
    suspend fun functionB() {
        val result = channel1.receive()
        println("ClassB received: $result")
        channel3.send(2)
    }
}

class ClassC(val channel2: Channel<Int>, val channel3: Channel<Int>, val channel4: Channel<Int>) {
    suspend fun functionC() {
        channel2.send(3)
        val result = channel3.receive()
        println("ClassC received: $result")
        channel4.send(4)
    }
}

suspend fun functionD(channel4: Channel<Int>) {
    val result = channel4.receive()
    println("FunctionD received: $result")
}

suspend fun functionE(classA: ClassA) {
    classA.functionA()
}

suspend fun functionF(classB: ClassB) {
    classB.functionB()
}

suspend fun functionG(classC: ClassC) {
    classC.functionC()
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()

    val classA = ClassA(channel1, channel2)
    val classB = ClassB(channel1, channel3)
    val classC = ClassC(channel2, channel3, channel4)

    launch { functionE(classA) }
    launch { functionF(classB) }
    launch { functionG(classC) }
    functionD(channel4)
}

class RunChecker321: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}