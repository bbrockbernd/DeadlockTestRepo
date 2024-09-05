/* 
{"deadlock":false,"nFunctions":6,"nCoroutines":1,"nChannels":7,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 7 different channels
- 1 different coroutines
- 2 different classes

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
package org.example.generated.test83
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Class1 {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()

    suspend fun sendData(channel: Channel<Int>, data: Int) {
        channel.send(data)
    }

    suspend fun receiveData(channel: Channel<Int>): Int {
        return channel.receive()
    }
}

class Class2 {
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>(1)

    suspend fun process1(channel: Channel<Int>) {
        val receivedData = channel.receive()
        channel4.send(receivedData * 2)
    }

    suspend fun process2(channel: Channel<Int>): Int {
        return channel.receive() + 1
    }
}

fun initiateChannels() {
    val class1 = Class1()
    val class2 = Class2()
    val channel6 = Channel<Int>()
    val channel7 = Channel<Int>(2)

    runBlocking {
        launch {
            class1.sendData(class1.channel1, 10)
            val data = class1.receiveData(class1.channel2)
            class1.sendData(channel6, data)
        }

        launch {
            val data = class1.receiveData(class1.channel1)
            class1.sendData(class1.channel2, data)
        }

        launch {
            class2.process1(class1.channel3)
            val processedData = class2.process2(channel7)
            class1.sendData(class1.channel3, processedData)
        }

        launch {
            val receivedData = class1.receiveData(channel6)
            channel7.send(receivedData)
        }

        launch {
            channel7.send(5)
            class2.process1(channel5)
        }
    }
}

fun main(): Unit {
    initiateChannels()
}