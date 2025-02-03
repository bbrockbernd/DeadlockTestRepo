/* 
{"deadlock":false,"nFunctions":6,"nCoroutines":1,"nChannels":8,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 8 different channels
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
package org.example.altered.test37
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassOne {
    val channelOne = Channel<Int>()
    val channelTwo = Channel<Int>()

    suspend fun functionOne() {
        channelOne.send(1)
        channelTwo.receive()
    }

    suspend fun functionTwo() {
        channelTwo.send(2)
        channelOne.receive()
    }
}

class ClassTwo {
    val channelThree = Channel<Int>()
    val channelFour = Channel<Int>()

    suspend fun functionThree() {
        channelThree.send(3)
        channelFour.receive()
    }

    suspend fun functionFour() {
        channelFour.send(4)
        channelThree.receive()
    }
}

class ClassThree {
    val channelFive = Channel<Int>()
    val channelSix = Channel<Int>()

    suspend fun functionFive() {
        channelFive.send(5)
        channelSix.receive()
    }

    suspend fun functionSix() {
        channelSix.send(6)
        channelFive.receive()
    }
}

fun main(): Unit = runBlocking {
    val classOne = ClassOne()
    val classTwo = ClassTwo()
    val classThree = ClassThree()

    val channelSeven = Channel<Int>()
    val channelEight = Channel<Int>()

    launch {
        classOne.functionOne()
        classOne.functionTwo()
    }

    launch {
        classTwo.functionThree()
        classTwo.functionFour()
    }

    launch {
        classThree.functionFive()
        classThree.functionSix()
    }

    channelSeven.send(7)
    channelEight.receive()
}

class RunChecker37: RunCheckerBase() {
    override fun block() = main()
}