/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":4,"nChannels":5,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 5 different channels
- 4 different coroutines
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
package org.example.altered.test869
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA(val channel1: Channel<Int>) {
    suspend fun sendDataToChannel1(data: Int) {
        channel1.send(data)
    }

    suspend fun receiveDataFromChannel1(): Int {
        return channel1.receive()
    }
}

class ClassB(val channel2: Channel<Int>, val channel3: Channel<Int>) {
    suspend fun sendDataToChannel2(data: Int) {
        channel2.send(data)
    }

    suspend fun receiveDataFromChannel3(): Int {
        return channel3.receive()
    }
}

class ClassC(val channel4: Channel<Int>, val channel5: Channel<Int>) {
    suspend fun sendDataToChannel4(data: Int) {
        channel4.send(data)
    }

    suspend fun receiveDataFromChannel5(): Int {
        return channel5.receive()
    }
}

fun function1(classA: ClassA, value: Int) = runBlocking {
    launch {
        classA.sendDataToChannel1(value)
    }
}

fun function2(classB: ClassB, classA: ClassA) = runBlocking {
    launch {
        val data = classA.receiveDataFromChannel1()
        classB.sendDataToChannel2(data * 2)
    }
}

fun function3(classC: ClassC, classB: ClassB) = runBlocking {
    launch {
        val data = classB.receiveDataFromChannel3() + 1
        classC.sendDataToChannel4(data)
    }
}

fun function4(classC: ClassC) = runBlocking {
    launch {
        val data = classC.receiveDataFromChannel5()
        println("Received final data: $data")
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()

    val classA = ClassA(channel1)
    val classB = ClassB(channel2, channel3)
    val classC = ClassC(channel4, channel5)
    
    function1(classA, 10)
    function2(classB, classA)
    function3(classC, classB)
    
    launch {
        channel3.send(20)
    }
    launch {
        channel5.send(30)
    }
    
    function4(classC)
}

class RunChecker869: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}