/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":8,"nChannels":8,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 8 different channels
- 8 different coroutines
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
package org.example.altered.test272
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class ClassA(val channelA: Channel<Int>, val channelB: Channel<Int>)
class ClassB(val channelC: Channel<String>, val channelD: Channel<String>)
class ClassC(val channelE: Channel<Double>, val channelF: Channel<Double>)
class ClassD(val channelG: Channel<Float>, val channelH: Channel<Float>)

fun functionOne(channel: Channel<Int>, value: Int) {
    GlobalScope.launch {
        channel.send(value)
    }
}

fun functionTwo(channel: Channel<String>, message: String) {
    GlobalScope.launch {
        channel.send(message)
    }
}

fun functionThree(channel: Channel<Double>, value: Double) {
    GlobalScope.launch {
        channel.send(value)
    }
}

fun main(): Unit = runBlocking {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()
    val channelC = Channel<String>()
    val channelD = Channel<String>()
    val channelE = Channel<Double>()
    val channelF = Channel<Double>()
    val channelG = Channel<Float>()
    val channelH = Channel<Float>()

    val classA = ClassA(channelA, channelB)
    val classB = ClassB(channelC, channelD)
    val classC = ClassC(channelE, channelF)
    val classD = ClassD(channelG, channelH)

    val job1 = launch {
        val value = classA.channelA.receive()
        classB.channelC.send("Value from channelA: $value")
    }

    val job2 = launch {
        val message = classB.channelD.receive()
        classC.channelE.send(message.length.toDouble())
    }

    val job3 = launch {
        val doubleValue = classC.channelF.receive()
        classD.channelG.send(doubleValue.toFloat())
    }

    val job4 = launch {
        val floatValue = classD.channelH.receive()
        classA.channelB.send(floatValue.toInt())
    }

    functionOne(classA.channelA, 42)
    functionTwo(classB.channelD, "Hello")
    functionThree(classC.channelF, 3.14)

    launch {
        val received = classD.channelG.receive()
        println("Received from classD channelG: $received")
    }

    launch {
        val received = classA.channelB.receive()
        println("Received from classA channelB: $received")
    }

    functionOne(classA.channelA, 100)
    functionOne(classA.channelB, 200)

    delay(1000) // Giving some time for coroutines to complete their execution
}

class RunChecker272: RunCheckerBase() {
    override fun block() = main()
}