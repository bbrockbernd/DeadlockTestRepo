/* 
{"deadlock":false,"nFunctions":7,"nCoroutines":5,"nChannels":7,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 7 different channels
- 5 different coroutines
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
package org.example.altered.test296
import org.example.altered.test296.RunChecker296.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()

    suspend fun sendToB(value: Int) {
        channelB.send(value)
    }

    suspend fun receiveFromA() {
        val value = channelA.receive()
        sendToB(value * 2)
    }
}

class ClassB {
    val channelC = Channel<Int>()
    val channelD = Channel<Int>()

    suspend fun sendToC(value: Int) {
        channelC.send(value)
    }

    suspend fun receiveFromD() {
        val value = channelD.receive()
        sendToC(value + 1)
    }
}

class ClassC {
    val channelE = Channel<Int>()
    val channelF = Channel<Int>()

    suspend fun sendToF(value: Int) {
        channelF.send(value)
    }

    suspend fun receiveFromE() {
        val value = channelE.receive()
        sendToF(value - 1)
    }
}

fun function1(classA: ClassA) = runBlocking(pool) {
    launch(pool) {
        classA.channelA.send(5)
    }
    launch(pool) {
        classA.receiveFromA()
    }
}

fun function2(classB: ClassB) = runBlocking(pool) {
    launch(pool) {
        classB.channelD.send(10)
    }
    launch(pool) {
        classB.receiveFromD()
    }
}

fun function3(classC: ClassC) = runBlocking(pool) {
    launch(pool) {
        classC.channelE.send(15)
    }
    launch(pool) {
        classC.receiveFromE()
    }
}

fun function4(classA: ClassA, classB: ClassB) = runBlocking(pool) {
    launch(pool) {
        classA.sendToB(20)
    }
    launch(pool) {
        classB.receiveFromD()
    }
}

fun function5(classB: ClassB, classC: ClassC) = runBlocking(pool) {
    launch(pool) {
        classB.sendToC(25)
    }
    launch(pool) {
        classC.receiveFromE()
    }
}

fun function6(classA: ClassA, classB: ClassB, classC: ClassC) = runBlocking(pool) {
    launch(pool) {
        classA.channelB.send(30)
    }
    launch(pool) {
        classB.receiveFromD()
    }
    launch(pool) {
        classC.channelF.receive()
    }
}

fun function7(classA: ClassA, classB: ClassB, classC: ClassC) = runBlocking(pool) {
    coroutineScope {
        function1(classA)
        function2(classB)
        function3(classC)
    }
}

fun main(): Unit = runBlocking(pool) {
    val classA = ClassA()
    val classB = ClassB()
    val classC = ClassC()
    
    launch(pool) { function1(classA) }
    launch(pool) { function2(classB) }
    launch(pool) { function3(classC) }
    launch(pool) { function4(classA, classB) }
    launch(pool) { function5(classB, classC) }
    launch(pool) { function6(classA, classB, classC) }
    launch(pool) { function7(classA, classB, classC) }
}

class RunChecker296: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}