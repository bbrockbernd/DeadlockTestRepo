/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":4,"nChannels":2,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 2 different channels
- 4 different coroutines
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
package org.example.altered.test580
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ProducerA(val channelA: Channel<Int>, val channelB: Channel<String>) {
    fun produceValues() {
        GlobalScope.launch {
            for (i in 1..5) {
                channelA.send(i)
            }
        }
        GlobalScope.launch {
            for (i in 1..5) {
                channelB.send("A$i")
            }
        }
    }
}

class ProducerB(val channelA: Channel<Int>, val channelB: Channel<String>) {
    fun produceOtherValues() {
        GlobalScope.launch {
            for (i in 6..10) {
                channelA.send(i)
            }
        }
        GlobalScope.launch {
            for (i in 6..10) {
                channelB.send("B$i")
            }
        }
    }
}

class Consumer(val channelA: Channel<Int>, val channelB: Channel<String>) {
    fun consume() {
        GlobalScope.launch {
            repeat(10) {
                println("Received from Channel A: ${channelA.receive()}")
            }
        }
        GlobalScope.launch {
            repeat(10) {
                println("Received from Channel B: ${channelB.receive()}")
            }
        }
    }
}

fun main(): Unit= runBlocking {
    val channelA = Channel<Int>()
    val channelB = Channel<String>()

    val producerA = ProducerA(channelA, channelB)
    val producerB = ProducerB(channelA, channelB)
    val consumer = Consumer(channelA, channelB)

    producerA.produceValues()
    producerB.produceOtherValues()
    consumer.consume()

    delay(1000) // Wait for coroutines to finish
}

class RunChecker580: RunCheckerBase() {
    override fun block() = main()
}