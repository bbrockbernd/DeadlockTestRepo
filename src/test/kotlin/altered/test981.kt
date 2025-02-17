/* 
{"deadlock":false,"nFunctions":1,"nCoroutines":2,"nChannels":5,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
- 5 different channels
- 2 different coroutines
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
package org.example.altered.test981
import org.example.altered.test981.RunChecker981.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ProducerA {
    val channelA: Channel<Int> = Channel()
    val channelB: Channel<Int> = Channel()

    suspend fun produceA() {
        channelA.send(1)
        channelB.send(2)
    }
}

class ProducerB {
    val channelC: Channel<String> = Channel()
    val channelD: Channel<String> = Channel()
    val channelE: Channel<String> = Channel()

    suspend fun produceB() {
        channelC.send("Hello")
        channelD.send("World")
        channelE.send("Kotlin")
    }
}

fun main(): Unit= runBlocking(pool) {
    val producerA = ProducerA()
    val producerB = ProducerB()

    launch(pool) {
        producerA.produceA()
        producerB.produceB()
    }

    launch(pool) {
        println(producerA.channelA.receive())
        println(producerA.channelB.receive())
        println(producerB.channelC.receive())
        println(producerB.channelD.receive())
        println(producerB.channelE.receive())
    }
}

class RunChecker981: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}