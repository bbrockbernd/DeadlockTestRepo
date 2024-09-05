/* 
{"deadlock":false,"nFunctions":6,"nCoroutines":2,"nChannels":6,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 6 different channels
- 2 different coroutines
- 5 different classes

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
package org.example.generated.test189
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

fun createChannelA() = Channel<Int>()
fun createChannelB() = Channel<String>()
fun createChannelC() = Channel<Double>()
fun createChannelD() = Channel<Boolean>()
fun createChannelE() = Channel<Float>()
fun createChannelF() = Channel<Char>()

class A(val channelA: Channel<Int>, val channelB: Channel<String>) {
    suspend fun operate() {
        repeat(5) {
            channelA.send(it)
            println("A: Sent $it to channelA")
            val received = channelB.receive()
            println("A: Received $received from channelB")
        }
    }
}

class B(val channelB: Channel<String>, val channelC: Channel<Double>) {
    suspend fun operate() {
        repeat(5) {
            channelB.send("Message $it")
            println("B: Sent Message $it to channelB")
            val received = channelC.receive()
            println("B: Received $received from channelC")
        }
    }
}

class C(val channelC: Channel<Double>, val channelD: Channel<Boolean>) {
    suspend fun operate() {
        repeat(5) {
            channelC.send(it.toDouble())
            println("C: Sent ${it.toDouble()} to channelC")
            val received = channelD.receive()
            println("C: Received $received from channelD")
        }
    }
}

class D(val channelD: Channel<Boolean>, val channelE: Channel<Float>) {
    suspend fun operate() {
        repeat(5) {
            channelD.send(it % 2 == 0)
            println("D: Sent ${it % 2 == 0} to channelD")
            val received = channelE.receive()
            println("D: Received $received from channelE")
        }
    }
}

class E(val channelE: Channel<Float>, val channelF: Channel<Char>) {
    suspend fun operate() {
        repeat(5) {
            channelE.send(it.toFloat())
            println("E: Sent ${it.toFloat()} to channelE")
            val received = channelF.receive()
            println("E: Received $received from channelF")
        }
    }
}

class F(val channelF: Channel<Char>, val channelA: Channel<Int>) {
    suspend fun operate() {
        repeat(5) {
            channelF.send('A' + it)
            println("F: Sent ${'A' + it} to channelF")
            val received = channelA.receive()
            println("F: Received $received from channelA")
        }
    }
}

fun main(): Unit= runBlocking {
    val channelA = createChannelA()
    val channelB = createChannelB()
    val channelC = createChannelC()
    val channelD = createChannelD()
    val channelE = createChannelE()
    val channelF = createChannelF()

    val a = A(channelA, channelB)
    val b = B(channelB, channelC)
    val c = C(channelC, channelD)
    val d = D(channelD, channelE)
    val e = E(channelE, channelF)
    val f = F(channelF, channelA)

    launch { a.operate() }
    launch { b.operate() }
    launch { c.operate() }
    launch { d.operate() }
    launch { e.operate() }
    launch { f.operate() }
}
