/* 
{"deadlock":true,"nFunctions":8,"nCoroutines":7,"nChannels":4,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
- 4 different channels
- 7 different coroutines
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
package org.example.altered.test176
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA(val channel1: Channel<Int>) {
    suspend fun sendToChannel1(value: Int) {
        channel1.send(value)
    }
    
    suspend fun receiveFromChannel1() {
        channel1.receive()
    }
}

class ClassB(val channel2: Channel<Int>) {
    suspend fun sendToChannel2(value: Int) {
        channel2.send(value)
    }
    
    suspend fun receiveFromChannel2() {
        channel2.receive()
    }
}

class ClassC(val channel3: Channel<Int>) {
    suspend fun sendToChannel3(value: Int) {
        channel3.send(value)
    }
    
    suspend fun receiveFromChannel3() {
        channel3.receive()
    }
}

class ClassD(val channel4: Channel<Int>) {
    suspend fun sendToChannel4(value: Int) {
        channel4.send(value)
    }
    
    suspend fun receiveFromChannel4() {
        channel4.receive()
    }
}

fun function1(classA: ClassA) = runBlocking {
    launch {
        classA.receiveFromChannel1()
    }
    launch {
        classA.sendToChannel1(100)
    }
}

fun function2(classB: ClassB) = runBlocking {
    launch {
        classB.receiveFromChannel2()
    }
    launch {
        classB.sendToChannel2(200)
    }
}

fun function3(classC: ClassC) = runBlocking {
    launch {
        classC.receiveFromChannel3()
    }
    launch {
        classC.sendToChannel3(300)
    }
}

fun function4(classD: ClassD) = runBlocking {
    launch {
        classD.receiveFromChannel4()
    }
    launch {
        classD.sendToChannel4(400)
    }
}

fun function5(classA: ClassA, classB: ClassB) = runBlocking {
    launch {
        classA.sendToChannel1(500)
    }
    launch {
        classB.receiveFromChannel2()
    }
}

fun function6(classB: ClassB, classC: ClassC) = runBlocking {
    launch {
        classB.sendToChannel2(600)
    }
    launch {
        classC.receiveFromChannel3()
    }
}

fun function7(classC: ClassC, classD: ClassD) = runBlocking {
    launch {
        classC.sendToChannel3(700)
    }
    launch {
        classD.receiveFromChannel4()
    }
}

fun function8(classD: ClassD, classA: ClassA) = runBlocking {
    launch {
        classD.sendToChannel4(800)
    }
    launch {
        classA.receiveFromChannel1()
    }
}

fun main(): Unit{
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    
    val classA = ClassA(channel1)
    val classB = ClassB(channel2)
    val classC = ClassC(channel3)
    val classD = ClassD(channel4)
    
    function1(classA)
    function2(classB)
    function3(classC)
    function4(classD)
    function5(classA, classB)
    function6(classB, classC)
    function7(classC, classD)
    function8(classD, classA)
}

class RunChecker176: RunCheckerBase() {
    override fun block() = main()
}