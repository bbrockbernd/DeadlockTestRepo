/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":7,"nChannels":7,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 7 different channels
- 7 different coroutines
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
package org.example.altered.test203
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(val channel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            channel.send(i)
        }
    }
}

class Consumer(val channel: Channel<Int>) {
    suspend fun consume() {
        for (i in 1..5) {
            channel.receive()
        }
    }
}

class Worker(val inputChannel: Channel<Int>, val outputChannel: Channel<Int>) {
    suspend fun work() {
        val item = inputChannel.receive()
        outputChannel.send(item * item)
    }
}

fun mainChannel(): Channel<Int> = Channel()

fun bufferChannel(capacity: Int): Channel<Int> = Channel(capacity)

suspend fun deadlockExample() = coroutineScope {
    val ch1 = mainChannel()
    val ch2 = bufferChannel(2)
    val ch3 = mainChannel()
    val ch4 = mainChannel()
    val ch5 = mainChannel()
    val ch6 = mainChannel()
    val ch7 = mainChannel()

    val producer1 = Producer(ch1)
    val consumer1 = Consumer(ch2)
    val worker1 = Worker(ch1, ch3)
    val worker2 = Worker(ch3, ch4)
    val worker3 = Worker(ch4, ch5)
    val worker4 = Worker(ch5, ch6)
    val worker5 = Worker(ch6, ch7)

    launch { producer1.produce() }
    launch { consumer1.consume() }
    launch { worker1.work() }
    launch { worker2.work() }
    launch { worker3.work() }
    launch { worker4.work() }
    launch { worker5.work() }
}

fun main(): Unit= runBlocking {
    deadlockExample()
}

class RunChecker203: RunCheckerBase() {
    override fun block() = main()
}