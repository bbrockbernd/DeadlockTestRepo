/* 
{"deadlock":true,"nFunctions":7,"nCoroutines":5,"nChannels":5,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 5 different channels
- 5 different coroutines
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
package org.example.altered.test455
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class A(val sendChannel: Channel<Int>, val receiveChannel: Channel<Int>) {
    suspend fun sendData(data: Int) {
        sendChannel.send(data)
    }
}

class B(val sendChannel: Channel<Int>, val receiveChannel: Channel<Int>) {
    suspend fun sendData(data: Int) {
        sendChannel.send(data)
    }
}

class C(val sendChannel: Channel<Int>, val receiveChannel: Channel<Int>) {
    suspend fun sendData(data: Int) {
        sendChannel.send(data)
    }
}

class D(val sendChannel: Channel<Int>, val receiveChannel: Channel<Int>) {
    suspend fun sendData(data: Int) {
        sendChannel.send(data)
    }
}

fun createChannels(): List<Channel<Int>> {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()
    return listOf(channel1, channel2, channel3, channel4, channel5)
}

fun startCoroutines(channels: List<Channel<Int>>) {
    runBlocking {
        launch { 
            val a = A(channels[0], channels[1])
            a.sendData(1)
        }
        launch { 
            val b = B(channels[1], channels[2])
            b.sendData(2)
        }
        launch { 
            val c = C(channels[2], channels[3])
            c.sendData(3)
        }
        launch { 
            val d = D(channels[3], channels[4])
            d.sendData(4)
        }
        launch { 
            val a = A(channels[4], channels[0])
            a.sendData(5) 
        }
    }
}

fun main(): Unit {
    val channels = createChannels()
    startCoroutines(channels)
}

class RunChecker455: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}