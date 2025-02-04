/* 
{"deadlock":false,"nFunctions":1,"nCoroutines":1,"nChannels":3,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
- 3 different channels
- 1 different coroutines
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
package org.example.altered.test543
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ChannelManager {
    val channel1 = Channel<Int>(1)
    val channel2 = Channel<String>(1)
    val channel3 = Channel<Double>(1)

    fun init() = runBlocking {
        val coroutine = launch {
            sendData()
            receiveData()
        }
        coroutine.join() // ensure coroutine completes
    }

    private suspend fun sendData() {
        channel1.send(42)
        channel2.send("Hello")
        channel3.send(3.14)
    }

    private suspend fun receiveData() {
        val intVal = channel1.receive()
        val strVal = channel2.receive()
        val doubleVal = channel3.receive()
        processData(intVal, strVal, doubleVal)
    }

    private fun processData(intVal: Int, strVal: String, doubleVal: Double) {
        println("Received Integer: $intVal")
        println("Received String: $strVal")
        println("Received Double: $doubleVal")
    }
}

fun main(): Unit{
    ChannelManager().init()
}

class RunChecker543: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}