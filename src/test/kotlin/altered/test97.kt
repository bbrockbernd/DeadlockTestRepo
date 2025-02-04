/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":5,"nChannels":3,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 3 different channels
- 5 different coroutines
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
package org.example.altered.test97
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(private val channel1: Channel<Int>, private val channel2: Channel<Int>) {
    suspend fun produceItems() {
        repeat(5) {
            channel1.send(it)
            channel2.send(it * 2)
        }
    }
}

class Consumer(private val channel1: Channel<Int>, private val channel2: Channel<Int>, private val channel3: Channel<String>) {
    suspend fun consumeAndProcess() {
        repeat(5) {
            val item1 = channel1.receive()
            val item2 = channel2.receive()
            channel3.send("Processed $item1 and $item2")
        }
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<String>()

    val producer = Producer(channel1, channel2)
    val consumer = Consumer(channel1, channel2, channel3)

    coroutineScope {
        launch { producer.produceItems() }
        launch { consumer.consumeAndProcess() }
        launch { for (msg in 1..5) println(channel3.receive()) }
    }
}

class RunChecker97: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}