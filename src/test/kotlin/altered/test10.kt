/* 
{"deadlock":false,"nFunctions":7,"nCoroutines":3,"nChannels":4,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
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
package org.example.altered.test10
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA {
    private val channelA = Channel<Int>()
    private val channelB = Channel<Int>()

    suspend fun sendToChannelA(value: Int) {
        channelA.send(value)
    }

    suspend fun receiveFromChannelA(): Int {
        return channelA.receive()
    }

    suspend fun sendToChannelB(value: Int) {
        channelB.send(value)
    }

    suspend fun receiveFromChannelB(): Int {
        return channelB.receive()
    }
}

class ClassB {
    private val channelC = Channel<String>()
    private val channelD = Channel<String>()

    suspend fun sendToChannelC(value: String) {
        channelC.send(value)
    }

    suspend fun receiveFromChannelC(): String {
        return channelC.receive()
    }

    suspend fun sendToChannelD(value: String) {
        channelD.send(value)
    }

    suspend fun receiveFromChannelD(): String {
        return channelD.receive()
    }
}

class ClassC {
    private val classA = ClassA()
    private val classB = ClassB()

    suspend fun function1() {
        classA.sendToChannelA(1)
    }

    suspend fun function2() {
        val value = classA.receiveFromChannelA()
        classA.sendToChannelB(value + 1)
    }

    suspend fun function3() {
        val value = classA.receiveFromChannelB()
        classB.sendToChannelC("Value: $value")
    }

    suspend fun function4() {
        val message = classB.receiveFromChannelC()
        classB.sendToChannelD(message + " processed")
    }
}

fun main(): Unit= runBlocking {
    val classC = ClassC()

    launch {
        classC.function1()
    }

    launch {
        classC.function2()
    }

    launch {
        classC.function3()
    }

    launch {
        classC.function4()
    }

    delay(1000L)
}

class RunChecker10: RunCheckerBase() {
    override fun block() = main()
}