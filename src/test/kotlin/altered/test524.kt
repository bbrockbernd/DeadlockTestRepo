/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":3,"nChannels":4,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 4 different channels
- 3 different coroutines
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
package org.example.altered.test524
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA {
    val channelA = Channel<Int>()
    fun sendToChannelA(value: Int) {
        GlobalScope.launch {
            channelA.send(value)
        }
    }

    suspend fun receiveFromChannelA(): Int {
        return channelA.receive()
    }
}

class ClassB {
    val channelB = Channel<String>()
    fun sendToChannelB(value: String) {
        GlobalScope.launch {
            channelB.send(value)
        }
    }

    suspend fun receiveFromChannelB(): String {
        return channelB.receive()
    }
}

class ClassC {
    val channelC = Channel<Double>()
    fun sendToChannelC(value: Double) {
        GlobalScope.launch {
            channelC.send(value)
        }
    }

    suspend fun receiveFromChannelC(): Double {
        return channelC.receive()
    }
}

fun sendValuesA(a: ClassA) {
    a.sendToChannelA(10)
}

fun sendValuesB(b: ClassB) {
    b.sendToChannelB("Kotlin")
}

suspend fun coroFuncA(a: ClassA, c1: Channel<Boolean>) {
    sendValuesA(a)
    c1.send(true)
}

suspend fun coroFuncB(b: ClassB, c2: Channel<Boolean>) {
    sendValuesB(b)
    c2.send(true)
}

suspend fun coroFuncC(c: ClassC, a: ClassA, b: ClassB, c3: Channel<Boolean>) {
    c.sendToChannelC(3.14)
    c.receiveFromChannelC()
    a.receiveFromChannelA()
    b.receiveFromChannelB()
    c3.send(true)
}

fun main(): Unit= runBlocking {
    val a = ClassA()
    val b = ClassB()
    val c = ClassC()
    val channelACompleted = Channel<Boolean>()
    val channelBCompleted = Channel<Boolean>()
    val channelCCompleted = Channel<Boolean>()

    launch {
        coroFuncA(a, channelACompleted)
    }

    launch {
        coroFuncB(b, channelBCompleted)
    }

    launch {
        coroFuncC(c, a, b, channelCCompleted)
    }

    channelACompleted.receive()
    channelBCompleted.receive()
    channelCCompleted.receive()
}

class RunChecker524: RunCheckerBase() {
    override fun block() = main()
}