/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":2,"nChannels":2,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 2 different channels
- 2 different coroutines
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
package org.example.generated.test568
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA(val channelA: Channel<Int>, val channelB: Channel<Int>) {
    suspend fun sendToA(value: Int) {
        channelA.send(value)
    }

    suspend fun sendToB(value: Int) {
        channelB.send(value)
    }
}

class ClassB(val channelA: Channel<Int>, val channelB: Channel<Int>) {
    suspend fun receiveFromA(): Int {
        return channelA.receive()
    }

    suspend fun receiveFromB(): Int {
        return channelB.receive()
    }
}

class ClassC(private val classA: ClassA, private val classB: ClassB) {
    suspend fun process() {
        val valueA = classB.receiveFromA()
        classA.sendToB(valueA * 2)
    }
}

fun firstFunction(classA: ClassA) {
    GlobalScope.launch {
        classA.sendToA(10)
    }
}

fun secondFunction(classC: ClassC) {
    GlobalScope.launch {
        classC.process()
    }
}

fun main(): Unit= runBlocking {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()

    val classA = ClassA(channelA, channelB)
    val classB = ClassB(channelA, channelB)
    val classC = ClassC(classA, classB)

    firstFunction(classA)
    secondFunction(classC)

    channelB.receive() // Blocking main to wait for processing
}