/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":2,"nChannels":5,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 5 different channels
- 2 different coroutines
- 0 different classes

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
package org.example.altered.test814
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.launch
import kotlinx.coroutines.channels.Channel

fun function1() = Channel<Int>(1)

fun function2() = Channel<Int>()

fun function3(channel: Channel<Int>) {
    runBlocking {
        launch {
            channel.send(3)
        }
    }
}

fun function4(channel1: Channel<Int>, channel2: Channel<Int>) {
    runBlocking {
        launch {
            val receivedValue = channel1.receive()
            channel2.send(receivedValue * 2)
        }
    }
}

fun function5(channel1: Channel<Int>, channel2: Channel<Int>, channel3: Channel<Int>) {
    runBlocking {
        launch {
            val firstValue = channel1.receive()
            val secondValue = channel2.receive()
            channel3.send(firstValue + secondValue)
        }
    }
}

fun main(): Unit{
    val channelA = function1()
    val channelB = function2()
    val channelC = function2()
    val channelD = function1()
    val channelE = function2()

    function3(channelA)
    function4(channelA, channelB)
    function5(channelB, channelC, channelD)
    function4(channelC, channelE)

    runBlocking {
        launch {
            println(channelD.receive())
            println(channelE.receive())
        }
    }
}

class RunChecker814: RunCheckerBase() {
    override fun block() = main()
}