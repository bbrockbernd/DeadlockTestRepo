/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":3,"nChannels":6,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 6 different channels
- 3 different coroutines
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
package org.example.altered.test408
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Processor1 {
    val channelA = Channel<Int>()
    val channelB = Channel<String>()

    fun sendToA(value: Int) {
        GlobalScope.launch {
            channelA.send(value)
        }
    }

    suspend fun receiveFromA(): Int {
        return channelA.receive()
    }

    suspend fun sendToB(value: String) {
        channelB.send(value)
    }

    fun receiveFromB() = GlobalScope.launch {
        println(channelB.receive())
    }
}

class Processor2 {
    val channelC = Channel<Double>(1)
    val channelD = Channel<Long>(1)

    suspend fun sendToC(value: Double) {
        channelC.send(value)
    }

    fun receiveFromC() = GlobalScope.launch {
        val received = channelC.receive()
        println(received)
        GlobalScope.launch {
            sendToD(received.toLong())
        }
    }

    suspend fun sendToD(value: Long) {
        channelD.send(value)
    }

    suspend fun receiveFromD(): Long {
        return channelD.receive()
    }
}

class Coordinator {
    val proc1 = Processor1()
    val proc2 = Processor2()
    val channelE = Channel<Int>()
    val channelF = Channel<String>(1)

    fun start() = runBlocking {
        coroutineScope {
            launch { 
                channelE.send(10) 
            }
            launch {
                val receivedE = channelE.receive()
                proc1.sendToA(receivedE)
                proc1.receiveFromB()
                proc2.receiveFromC()
            }
            launch {
                proc1.receiveFromA()
                proc1.sendToB("Hello")
                channelF.send("World")
                println(channelF.receive())
            }
        }
    }
}

fun main(): Unit {
    val coordinator = Coordinator()
    coordinator.start()
}

class RunChecker408: RunCheckerBase() {
    override fun block() = main()
}