/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":4,"nChannels":6,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 6 different channels
- 4 different coroutines
- 1 different classes

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
package org.example.generated.test205
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class ChannelManager {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()
    val channelC = Channel<Int>()
    val channelD = Channel<Int>(5)
    val channelE = Channel<Int>()
    val channelF = Channel<Int>()

    suspend fun sendValuesToChannelA() {
        for (i in 1..5) {
            channelA.send(i)
        }
    }

    suspend fun sendValuesToChannelB() {
        for (i in 1..5) {
            channelB.send(i + 5)
        }
    }

    suspend fun receiveAndProcessValuesFromChannelAB() = coroutineScope {
        launch {
            repeat(5) {
                val a = channelA.receive()
                val b = channelB.receive()
                channelD.send(a + b)
            }
        }
    }

    suspend fun transferValuesFromChannelDToE() {
        repeat(5) {
            val d = channelD.receive()
            channelE.send(d)
        }
    }

    suspend fun finalProcessing() = coroutineScope {
        launch {
            repeat(5) {
                val e = channelE.receive()
                channelF.send(e * 2)
            }
        }
    }
}

fun main(): Unit= runBlocking {
    val manager = ChannelManager()

    launch { manager.sendValuesToChannelA() }
    launch { manager.sendValuesToChannelB() }
    launch { manager.receiveAndProcessValuesFromChannelAB() }
    launch { manager.transferValuesFromChannelDToE() }
    launch { manager.finalProcessing() }

    repeat(5) {
        println("Final Result: ${manager.channelF.receive()}")
    }
}