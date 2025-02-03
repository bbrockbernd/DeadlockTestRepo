/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":2,"nChannels":4,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 4 different channels
- 2 different coroutines
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
package org.example.altered.test675
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
}

class ClassB {
    val channel3 = Channel<Int>()
}

class ClassC {
    val channel4 = Channel<Int>()
}

fun function1(channel1: Channel<Int>, channel4: Channel<Int>) = runBlocking {
    launch {
        val value = channel1.receive()
        channel4.send(value)
        println("function1: Sent $value from channel1 to channel4")
    }
}

fun function2(channel2: Channel<Int>, channel3: Channel<Int>) = runBlocking {
    launch {
        val value = channel2.receive()
        channel3.send(value)
        println("function2: Sent $value from channel2 to channel3")
    }
}

fun function3(channel3: Channel<Int>, channel4: Channel<Int>) = runBlocking {
    launch {
        val value = channel3.receive()
        channel4.send(value)
        println("function3: Sent $value from channel3 to channel4")
    }
}

fun main(): Unit= runBlocking<Unit> {
    val classA = ClassA()
    val classB = ClassB()
    val classC = ClassC()

    launch {
        function1(classA.channel1, classC.channel4)
        function2(classA.channel2, classB.channel3)
    }

    launch {
        function3(classB.channel3, classC.channel4)
    }

    // Initiating channels to create a deadlock
    classA.channel1.send(1)
    classA.channel2.send(2)

    // These sends will cause a deadlock
    classB.channel3.send(3)
    classC.channel4.send(4)
}

class RunChecker675: RunCheckerBase() {
    override fun block() = main()
}