/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":1,"nChannels":3,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 3 different channels
- 1 different coroutines
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
package org.example.altered.test361
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA(val channel: Channel<Int>)
class ClassB(val channel: Channel<String>)
class ClassC(val channel: Channel<Double>)

fun producer1(channelA: Channel<Int>, channelB: Channel<String>) {
    runBlocking {
        launch {
            for (i in 1..5) {
                channelA.send(i)
            }
            channelB.send("Done")
        }
    }
}

fun producer2(channelC: Channel<Double>) {
    runBlocking {
        launch {
            for (i in 1..5) {
                channelC.send(i.toDouble())
            }
        }
    }
}

fun main(): Unit{
    val channelA = Channel<Int>()
    val channelB = Channel<String>()
    val channelC = Channel<Double>()
    
    val classA = ClassA(channelA)
    val classB = ClassB(channelB)
    val classC = ClassC(channelC)

    runBlocking {
        launch {
            producer1(classA.channel, classB.channel)
            producer2(classC.channel)

            repeat(5) {
                println("Received from ClassA: ${classA.channel.receive()}")
            }

            println("Received from ClassB: ${classB.channel.receive()}")

            repeat(5) {
                println("Received from ClassC: ${classC.channel.receive()}")
            }
        }
    }
}

class RunChecker361: RunCheckerBase() {
    override fun block() = main()
}