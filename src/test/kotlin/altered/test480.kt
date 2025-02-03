/* 
{"deadlock":true,"nFunctions":2,"nCoroutines":5,"nChannels":7,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
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
package org.example.altered.test480
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA(val channel1: Channel<Int>, val channel2: Channel<Int>)

class ClassB(val channel3: Channel<Int>, val channel4: Channel<Int>)

class ClassC(val channel5: Channel<Int>, val channel6: Channel<Int>, val channel7: Channel<Int>)

fun functionOne(channel1: Channel<Int>, channel2: Channel<Int>) {
    runBlocking {
        launch {
            val data = channel1.receive() // Waiting to receive
            channel2.send(data) // Trying to send
        }
    }
}

fun functionTwo(channel3: Channel<Int>, channel4: Channel<Int>, channel5: Channel<Int>) {
    runBlocking {
        launch {
            val data = channel3.receive() // Waiting to receive
            channel4.send(data) // Trying to send
        }
        launch {
            val data = channel5.receive() // Waiting to receive
            channel3.send(data) // Trying to send
        }
    }
}

fun main(): Unit{
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()
    val channel6 = Channel<Int>()
    val channel7 = Channel<Int>()

    val classA = ClassA(channel1, channel2)
    val classB = ClassB(channel3, channel4)
    val classC = ClassC(channel5, channel6, channel7)

    runBlocking {
        launch {
            val data = classA.channel1.receive() // Waiting to receive
            classB.channel3.send(data) // Trying to send
        }
        functionOne(classA.channel1, classA.channel2)
        functionTwo(classB.channel3, classB.channel4, classC.channel5)
    }
}

class RunChecker480: RunCheckerBase() {
    override fun block() = main()
}