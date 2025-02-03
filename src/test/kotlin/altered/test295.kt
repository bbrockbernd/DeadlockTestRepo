/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":5,"nChannels":5,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 5 different channels
- 5 different coroutines
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
package org.example.altered.test295
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA {
    val channelA = Channel<Int>()
    suspend fun sendA(value: Int) {
        channelA.send(value)
    }
}

class ClassB {
    val channelB = Channel<Int>()
    suspend fun sendB(value: Int) {
        channelB.send(value)
    }
}

class ClassC {
    val channelC = Channel<Int>()
    suspend fun sendC(value: Int) {
        channelC.send(value)
    }
}

class ClassD {
    val channelD = Channel<Int>()
    suspend fun sendD(value: Int) {
        channelD.send(value)
    }
}

class ClassE {
    val channelE = Channel<Int>()
    suspend fun sendE(value: Int) {
        channelE.send(value)
    }
}

suspend fun functionA(classA: ClassA, classB: ClassB) {
    coroutineScope {
        launch {
            for (i in 1..5) {
                classA.sendA(i)
            }
        }
        launch {
            repeat(5) {
                val value = classA.channelA.receive()
                classB.sendB(value)
            }
        }
    }
}

suspend fun functionB(classC: ClassC, classD: ClassD) {
    coroutineScope {
        launch {
            for (i in 5 downTo 1) {
                classC.sendC(i)
            }
        }
        launch {
            repeat(5) {
                val value = classC.channelC.receive()
                classD.sendD(value)
            }
        }
    }
}

suspend fun functionC(classA: ClassA, classE: ClassE) {
    coroutineScope {
        launch {
            repeat(5) {
                val value = classA.channelA.receive()
                classE.sendE(value)
            }
        }
    }
}

suspend fun functionD(classE: ClassE) {
    coroutineScope {
        launch {
            repeat(5) {
                val value = classE.channelE.receive()
                println("Received from classE: $value")
            }
        }
    }
}

fun main(): Unit = runBlocking {
    val classA = ClassA()
    val classB = ClassB()
    val classC = ClassC()
    val classD = ClassD()
    val classE = ClassE()

    launch { functionA(classA, classB) }
    launch { functionB(classC, classD) }
    launch { functionC(classA, classE) }
    launch { functionD(classE) }
    
    // Simulate other operations that keep the main coroutine running
    delay(1000L)
}

class RunChecker295: RunCheckerBase() {
    override fun block() = main()
}