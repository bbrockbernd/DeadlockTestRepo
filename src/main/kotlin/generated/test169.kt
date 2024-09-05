/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":1,"nChannels":8,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 8 different channels
- 1 different coroutines
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
package org.example.generated.test169
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class TypeA(val channelA: Channel<Int>) {
    suspend fun sendToA(value: Int) {
        channelA.send(value)
    }

    suspend fun receiveFromA(): Int {
        return channelA.receive()
    }
}

class TypeB(val channelB: Channel<Int>) {
    suspend fun sendToB(value: Int) {
        channelB.send(value)
    }

    suspend fun receiveFromB(): Int {
        return channelB.receive()
    }
}

class TypeC(val channelC: Channel<Int>) {
    suspend fun sendToC(value: Int) {
        channelC.send(value)
    }

    suspend fun receiveFromC(): Int {
        return channelC.receive()
    }
}

class TypeD(val channelD: Channel<Int>) {
}

class TypeE(val channelE: Channel<Int>) {
}

fun processA(typeA: TypeA, typeD: TypeD) {
    runBlocking {
        launch {
            val msg = typeA.receiveFromA()
            (typeD.channelD).send(msg)
        }
    }
}

fun processB(typeB: TypeB, typeE: TypeE) {
    runBlocking {
        launch {
            val msg = typeB.receiveFromB()
            (typeE.channelE).send(msg)
        }
    }
}

fun processC(typeC: TypeC, typeA: TypeA, typeB: TypeB) {
    runBlocking {
        launch {
            val msgFromC = typeC.receiveFromC()
            typeA.sendToA(msgFromC)
            val msgFromA = typeA.receiveFromA()
            typeB.sendToB(msgFromA)
        }
    }
}

fun main(): Unit = runBlocking {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()
    val channelC = Channel<Int>()
    val channelD = Channel<Int>()
    val channelE = Channel<Int>()
    val channelF = Channel<Int>()
    val channelG = Channel<Int>()
    val channelH = Channel<Int>()

    val typeA = TypeA(channelA)
    val typeB = TypeB(channelB)
    val typeC = TypeC(channelC)
    val typeD = TypeD(channelD)
    val typeE = TypeE(channelE)

    launch {
        typeC.sendToC(42)
        processC(typeC, typeA, typeB)
        processA(typeA, typeD)
        processB(typeB, typeE)
    }
}