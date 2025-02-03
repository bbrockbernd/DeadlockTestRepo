/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":2,"nChannels":6,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 6 different channels
- 2 different coroutines
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
package org.example.altered.test99
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA(val channel1: Channel<Int>, val channel2: Channel<Int>)
class ClassB(val channel3: Channel<String>, val channel4: Channel<String>)
class ClassC(val channel5: Channel<Double>, val channel6: Channel<Double>)
class ClassD(val value: Int)

fun initClassA(classA: ClassA) {
    GlobalScope.launch {
        classA.channel1.send(42)
        val receivedValue = classA.channel2.receive()
        println("ClassA received: $receivedValue")
    }
}

fun initClassB(classB: ClassB) {
    GlobalScope.launch {
        classB.channel3.send("Hello")
        val receivedValue = classB.channel4.receive()
        println("ClassB received: $receivedValue")
    }
}

fun initClassC(classC: ClassC) {
    GlobalScope.launch {
        classC.channel5.send(3.14)
        val receivedValue = classC.channel6.receive()
        println("ClassC received: $receivedValue")
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<String>()
    val channel4 = Channel<String>()
    val channel5 = Channel<Double>()
    val channel6 = Channel<Double>()

    val classA = ClassA(channel1, channel2)
    val classB = ClassB(channel3, channel4)
    val classC = ClassC(channel5, channel6)

    val classD = ClassD(1)

    initClassA(classA)
    initClassB(classB)
    initClassC(classC)

    launch {
        val receivedFromA = classA.channel1.receive()
        classA.channel2.send(receivedFromA * classD.value)
    }

    launch {
        val receivedFromB = classB.channel3.receive()
        classB.channel4.send("$receivedFromB World")
    }

    launch {
        val receivedFromC = classC.channel5.receive()
        classC.channel6.send(receivedFromC * 2)
    }
}

class RunChecker99: RunCheckerBase() {
    override fun block() = main()
}