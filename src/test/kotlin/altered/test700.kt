/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":3,"nChannels":4,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 4 different channels
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
package org.example.altered.test700
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(val channel: Channel<String>) {
    suspend fun produceMessages() {
        repeat(3) {
            channel.send("Message $it")
        }
        channel.close()
    }
}

class Consumer(val channel: Channel<String>, val outputChannel: Channel<String>) {
    suspend fun consumeMessages() {
        for (message in channel) {
            outputChannel.send(message)
        }
        outputChannel.close()
    }
}

class Printer(val channel: Channel<String>) {
    suspend fun printMessages() {
        for (message in channel) {
            println(message)
        }
    }
}

fun main(): Unit= runBlocking<Unit> {
    val channel1 = Channel<String>()
    val channel2 = Channel<String>()
    val channel3 = Channel<String>()
    val channel4 = Channel<String>()

    val producer1 = Producer(channel1)
    val producer2 = Producer(channel2)

    val consumer1 = Consumer(channel1, channel3)
    val consumer2 = Consumer(channel2, channel4)

    val printer1 = Printer(channel3)
    val printer2 = Printer(channel4)

    launch { producer1.produceMessages() }
    launch { producer2.produceMessages() }

    launch { consumer1.consumeMessages() }
    launch { consumer2.consumeMessages() }

    launch { printer1.printMessages() }
    launch { printer2.printMessages() }
}

class RunChecker700: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}