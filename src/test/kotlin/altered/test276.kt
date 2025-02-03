/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":6,"nChannels":4,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 4 different channels
- 6 different coroutines
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
package org.example.altered.test276
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class ClassA(val channel: Channel<Int>) {
    suspend fun sendData(data: Int) {
        channel.send(data)
    }

    suspend fun receiveData(): Int {
        return channel.receive()
    }
}

class ClassB(val channel: Channel<Int>) {
    suspend fun sendData(data: Int) {
        channel.send(data)
    }

    suspend fun receiveData(): Int {
        return channel.receive()
    }
}

class ClassC(val channel: Channel<Int>) {
    suspend fun sendData(data: Int) {
        channel.send(data)
    }

    suspend fun receiveData(): Int {
        return channel.receive()
    }
}

fun function1(channelA: Channel<Int>, channelB: Channel<Int>) {
    runBlocking {
        launch {
            val data = channelA.receive()
            channelB.send(data)
        }
    }
}

fun function2(channelB: Channel<Int>, channelC: Channel<Int>) {
    runBlocking {
        launch {
            val data = channelB.receive()
            channelC.send(data)
        }
    }
}

fun function3(channelC: Channel<Int>, channelD: Channel<Int>) {
    runBlocking {
        launch {
            val data = channelC.receive()
            channelD.send(data)
        }
    }
}

fun function4(channelD: Channel<Int>, channelA: Channel<Int>) {
    runBlocking {
        launch {
            val data = channelD.receive()
            channelA.send(data)
        }
    }
}

fun function5(channelA: Channel<Int>) {
    runBlocking {
        launch {
            channelA.send(1)
        }
    }
}

fun main(): Unit{
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()
    val channelC = Channel<Int>()
    val channelD = Channel<Int>()

    val classA = ClassA(channelA)
    val classB = ClassB(channelB)
    val classC = ClassC(channelC)

    runBlocking {
        launch { function1(channelA, channelB) }
        launch { function2(channelB, channelC) }
        launch { function3(channelC, channelD) }
        launch { function4(channelD, channelA) }
        launch { function5(channelA) }
    }
}

class RunChecker276: RunCheckerBase() {
    override fun block() = main()
}