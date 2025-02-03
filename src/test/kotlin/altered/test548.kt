/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":2,"nChannels":4,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 4 different channels
- 2 different coroutines
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
package org.example.altered.test548
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Coordinator {
    private val channelA = Channel<Int>()
    private val channelB = Channel<Int>()
    private val channelC = Channel<Int>()
    private val channelD = Channel<Int>()

    fun sendValues() {
        CoroutineScope(Dispatchers.Default).launch {
            channelA.send(1)
            channelB.send(2)
        }
    }

    suspend fun receiveFromAAndSendToC() {
        val value = channelA.receive()
        channelC.send(value + 3)
    }

    suspend fun receiveFromBAndSendToD() {
        val value = channelB.receive()
        channelD.send(value + 4)
    }

    fun startCoroutines() {
        CoroutineScope(Dispatchers.Default).launch {
            receiveFromAAndSendToC()
        }
        CoroutineScope(Dispatchers.Default).launch {
            receiveFromBAndSendToD()
        }
    }

    suspend fun printFinalValues() {
        val valueC = channelC.receive()
        val valueD = channelD.receive()
        println("Value from C: $valueC, Value from D: $valueD")
    }
}

fun main(): Unit= runBlocking {
    val coordinator = Coordinator()
    coordinator.sendValues()
    coordinator.startCoroutines()
    coroutineScope {
        coordinator.printFinalValues()
    }
}

class RunChecker548: RunCheckerBase() {
    override fun block() = main()
}