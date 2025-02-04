/* 
{"deadlock":false,"nFunctions":8,"nCoroutines":7,"nChannels":6,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
- 6 different channels
- 7 different coroutines
- 4 different classes

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
package org.example.altered.test33
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA {
    val channelA = Channel<Int>(5)
    suspend fun sendA(value: Int) {
        channelA.send(value)
    }
    suspend fun receiveA(): Int {
        return channelA.receive()
    }
}

class ClassB {
    val channelB = Channel<String>()
    suspend fun sendB(value: String) {
        channelB.send(value)
    }
    suspend fun receiveB(): String {
        return channelB.receive()
    }
}

class ClassC {
    val channelC = Channel<Double>(Channel.UNLIMITED)
    suspend fun sendC(value: Double) {
        channelC.send(value)
    }
    suspend fun receiveC(): Double {
        return channelC.receive()
    }
}

class ClassD {
    val channelD = Channel<Long>(1)
    suspend fun sendD(value: Long) {
        channelD.send(value)
    }
    suspend fun receiveD(): Long {
        return channelD.receive()
    }
}

suspend fun function1(a: ClassA, b: ClassB) {
    a.sendA(42)
    println("Received in function1: ${b.receiveB()}")
}

suspend fun function2(c: ClassC, d: ClassD) {
    d.sendD(123456789L)
    println("Received in function2: ${c.receiveC()}")
}

suspend fun function3(a: ClassA, c: ClassC) {
    c.sendC(3.1415)
    println("Received in function3: ${a.receiveA()}")
}

suspend fun function4(b: ClassB, d: ClassD) {
    b.sendB("Hello")
    println("Received in function4: ${d.receiveD()}")
}

fun main(): Unit= runBlocking {
    val a = ClassA()
    val b = ClassB()
    val c = ClassC()
    val d = ClassD()

    launch { function1(a, b) }
    launch { function2(c, d) }
    launch { function3(a, c) }
    launch { function4(b, d) }
    launch { function1(a, b) }
    launch { function2(c, d) }
    launch { function3(a, c) }

    delay(1000L)
}

class RunChecker33: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}