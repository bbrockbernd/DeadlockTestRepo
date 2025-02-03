/* 
{"deadlock":true,"nFunctions":7,"nCoroutines":3,"nChannels":4,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 4 different channels
- 3 different coroutines
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
package org.example.altered.test185
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA(val channel1: Channel<Int>, val channel2: Channel<Int>)
class ClassB(val channel3: Channel<Int>, val channel4: Channel<Int>)
class ClassC(val channel1: Channel<Int>, val channel4: Channel<Int>)
class ClassD {
    lateinit var channel: Channel<Int>
}

fun function1(a: ClassA) {
    runBlocking {
        launch {
            a.channel1.send(1)
            a.channel2.receive()
        }
    }
}

fun function2(b: ClassB) {
    runBlocking {
        launch {
            b.channel3.send(2)
            b.channel4.receive()
        }
    }
}

fun function3(c: ClassC) {
    runBlocking {
        launch {
            c.channel1.send(3)
            c.channel4.receive()
        }
    }
}

fun function4(d: ClassD) {
    runBlocking {
        launch {
            d.channel.send(4)
        }
    }
}

fun function5(a: ClassA, b: ClassB) {
    function1(a)
    function2(b)
}

fun function6(a: ClassA, c: ClassC) {
    function1(a)
    function3(c)
}

fun function7(d: ClassD) {
    runBlocking {
        launch {
            val message = d.channel.receive()
            println("Received: $message")
        }
    }
}

fun main(): Unit{
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    
    val classA = ClassA(channel1, channel2)
    val classB = ClassB(channel3, channel4)
    val classC = ClassC(channel1, channel4)
    val classD = ClassD().apply { channel = channel3 }

    runBlocking {
        launch { function5(classA, classB) }
        launch { function6(classA, classC) }
        launch { function7(classD) }
    }
}

class RunChecker185: RunCheckerBase() {
    override fun block() = main()
}