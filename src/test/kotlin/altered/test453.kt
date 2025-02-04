/* 
{"deadlock":true,"nFunctions":6,"nCoroutines":7,"nChannels":2,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 2 different channels
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
package org.example.altered.test453
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class DataClassA(val channelA: Channel<Int>, val channelB: Channel<Int>) {
    suspend fun sendDataToB(data: Int) {
        channelA.send(data)
    }
}

class DataClassB(val channelA: Channel<Int>, val channelB: Channel<Int>) {
    suspend fun receiveDataFromA() {
        channelA.receive()
    }

    suspend fun sendDataToA(data: Int) {
        channelB.send(data)
    }
}

class DataClassC(val channelA: Channel<Int>, val channelB: Channel<Int>) {
    suspend fun receiveDataFromB() {
        channelB.receive()
    }
}

class DataClassD

fun function1(channelA: Channel<Int>, channelB: Channel<Int>) {
    val dataClassA = DataClassA(channelA, channelB)
    runBlocking {
        launch {
            dataClassA.sendDataToB(1)
        }
    }
}

fun function2(channelA: Channel<Int>, channelB: Channel<Int>) {
    val dataClassB = DataClassB(channelA, channelB)
    runBlocking {
        launch {
            dataClassB.receiveDataFromA()
        }
    }
}

fun function3(channelA: Channel<Int>, channelB: Channel<Int>) {
    val dataClassB = DataClassB(channelA, channelB)
    runBlocking {
        launch {
            dataClassB.sendDataToA(2)
        }
    }
}

fun function4(channelA: Channel<Int>, channelB: Channel<Int>) {
    val dataClassC = DataClassC(channelA, channelB)
    runBlocking {
        launch {
            dataClassC.receiveDataFromB()
        }
    }
}

fun function5(channelA: Channel<Int>, channelB: Channel<Int>) {
    runBlocking {
        launch {
            function6()
        }
    }
}

fun function6() {
    runBlocking {
        launch {
            delay(1000L) // Just a delay for some operation
        }
    }
}

fun main(): Unit {
    val channelA = Channel<Int>(1) // Buffered channel to potentially avoid immediate deadlock
    val channelB = Channel<Int>(1) // Buffered channel to potentially avoid immediate deadlock

    runBlocking {
        function1(channelA, channelB)
        function2(channelA, channelB)
        function3(channelA, channelB)
        function4(channelA, channelB)
        function5(channelA, channelB)
        function6()
    }
}

class RunChecker453: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}