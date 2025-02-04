/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":4,"nChannels":4,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 4 different channels
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
package org.example.altered.test883
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(private val channel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..3) {
            channel.send(i)
        }
    }
}

class Consumer(private val channel: Channel<Int>) {
    suspend fun consume() {
        while (true) {
            val received = channel.receive()
            println("Consumed $received")
        }
    }
}

class Manager(private val inputChannel: Channel<Int>, private val outputChannel: Channel<Int>) {
    suspend fun manage() {
        while (true) {
            val value = inputChannel.receive()
            outputChannel.send(value * 2)
        }
    }
}

fun initProducer(channel: Channel<Int>) = Producer(channel)
fun initConsumer(channel: Channel<Int>) = Consumer(channel)
fun initManager(inputChannel: Channel<Int>, outputChannel: Channel<Int>) = Manager(inputChannel, outputChannel)

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()

    val producer1 = initProducer(channel1)
    val producer2 = initProducer(channel2)
    val manager = initManager(channel1, channel3)
    val consumer = initConsumer(channel4)

    launch { producer1.produce() }
    launch { producer2.produce() }
    launch { manager.manage() }
    launch { consumer.consume() }

    delay(5000)  // To allow some time for deadlock to occur
    println("Main coroutine completed")
}

class RunChecker883: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}