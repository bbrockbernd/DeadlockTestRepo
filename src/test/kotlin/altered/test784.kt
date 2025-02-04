/* 
{"deadlock":false,"nFunctions":1,"nCoroutines":4,"nChannels":5,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
- 5 different channels
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
package org.example.altered.test784
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(val channel: Channel<Int>) {
    suspend fun produce() {
        repeat(3) { channel.send(it) }
    }
}

class Consumer(val inputChannel: Channel<Int>, val outputChannel: Channel<Int>) {
    suspend fun consume() {
        repeat(3) { outputChannel.send(inputChannel.receive() * 2) }
    }
}

class Printer(val channel: Channel<Int>) {
    suspend fun print() {
        repeat(3) { println(channel.receive()) }
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()

    val producer = Producer(channel1)
    val consumer1 = Consumer(channel1, channel2)
    val consumer2 = Consumer(channel2, channel3)
    val consumer3 = Consumer(channel3, channel4)
    val printer = Printer(channel4)

    coroutineScope {
        launch { producer.produce() }
        launch { consumer1.consume() }
        launch { consumer2.consume() }
        launch { consumer3.consume() }
        launch { printer.print() }
    }
}

class RunChecker784: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}