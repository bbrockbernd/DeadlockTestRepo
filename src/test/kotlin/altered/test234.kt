/* 
{"deadlock":false,"nFunctions":6,"nCoroutines":3,"nChannels":4,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 4 different channels
- 3 different coroutines
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
package org.example.altered.test234
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class Producer(val ch1: SendChannel<Int>, val ch2: SendChannel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            ch1.send(i)
            ch2.send(i * 2)
        }
    }
}

class Consumer(val ch1: ReceiveChannel<Int>, val ch2: ReceiveChannel<Int>) {
    suspend fun consume() {
        for (i in 1..10) {
            println(ch1.receive())
        }
        for (i in 1..10) {
            println(ch2.receive())
        }
    }
}

fun produceInChannel(ch1: SendChannel<Int>, ch2: SendChannel<Int>) = runBlocking {
    val producer = Producer(ch1, ch2)
    producer.produce()
}

fun consumeInChannel(ch1: ReceiveChannel<Int>, ch2: ReceiveChannel<Int>) = runBlocking {
    val consumer = Consumer(ch1, ch2)
    consumer.consume()
}

fun separateProcessing(ch: ReceiveChannel<Int>) = runBlocking {
    for (i in 1..5) {
        println("Separate Processing: ${ch.receive()}")
    }
}

fun main(): Unit = runBlocking {
    val ch1 = Channel<Int>(5)
    val ch2 = Channel<Int>(5)
    val ch3 = Channel<Int>(5)
    val ch4 = Channel<Int>(5)

    launch { produceInChannel(ch1, ch2) }
    launch { consumeInChannel(ch1, ch2) }
    launch { separateProcessing(ch3) }
}

class RunChecker234: RunCheckerBase() {
    override fun block() = main()
}