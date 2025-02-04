/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":7,"nChannels":7,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 7 different channels
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
package org.example.altered.test140
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA(val channel1: Channel<Int>, val channel2: Channel<Int>)
class ClassB(val channel3: Channel<Int>, val channel4: Channel<Int>)
class ClassC(val channel5: Channel<Int>, val channel6: Channel<Int>)
class ClassD(val channel7: Channel<Int>)

fun function1(classA: ClassA) = runBlocking {
    launch {
        val value = classA.channel1.receive()
        println("Function1 received: $value")
        classA.channel2.send(value + 1)
    }
}

fun function2(classB: ClassB) = runBlocking {
    launch {
        val value = classB.channel3.receive()
        println("Function2 received: $value")
        classB.channel4.send(value + 2)
    }
}

fun function3(classC: ClassC) = runBlocking {
    launch {
        val value = classC.channel5.receive()
        println("Function3 received: $value")
        classC.channel6.send(value + 3)
    }
}

fun function4(classD: ClassD) = runBlocking {
    launch {
        val value = classD.channel7.receive()
        println("Function4 received: $value")
        classD.channel7.send(value + 4)
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()
    val channel6 = Channel<Int>()
    val channel7 = Channel<Int>()

    val classA = ClassA(channel1, channel2)
    val classB = ClassB(channel3, channel4)
    val classC = ClassC(channel5, channel6)
    val classD = ClassD(channel7)

    launch { function1(classA) }
    launch { function2(classB) }
    launch { function3(classC) }
    launch { function4(classD) }

    launch {
        channel1.send(1)
        println("Main sent to channel1")
    }

    launch {
        val value = channel2.receive()
        println("Main received from channel2: $value")
        channel3.send(value)
    }

    launch {
        val value = channel4.receive()
        println("Main received from channel4: $value")
        channel5.send(value)
    }

    launch {
        val value = channel6.receive()
        println("Main received from channel6: $value")
        channel7.send(value)
    }
}

class RunChecker140: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}