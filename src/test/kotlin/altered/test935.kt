/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":1,"nChannels":3,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 3 different channels
- 1 different coroutines
- 1 different classes

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
package org.example.altered.test935
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Communicator(val channel1: Channel<Int>, val channel2: Channel<Int>) {
    suspend fun sendData(data: Int) {
        channel1.send(data)
    }
    
    suspend fun receiveData(): Int {
        return channel2.receive()
    }
}

fun CoroutineScope.producer(channel: Channel<Int>) {
    launch {
        repeat(5) {
            println("Producer sending $it")
            channel.send(it)
        }
        channel.close()
    }
}

suspend fun consumer(channel: Channel<Int>) {
    while (!channel.isClosedForReceive) {
        val value = channel.receiveCatching().getOrNull() ?: break
        println("Consumer received $value")
    }
}

fun CoroutineScope.intermittentProcessor(inputChannel: Channel<Int>, outputChannel: Channel<Int>) {
    launch {
        for (item in inputChannel) {
            println("Processor converting $item to ${item * 2}")
            outputChannel.send(item * 2)
        }
        outputChannel.close()
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()

    val communicator = Communicator(channel1, channel3)

    producer(channel1)
    intermittentProcessor(channel1, channel2)
    launch { consumer(channel2) }
    launch { 
        communicator.sendData(42) 
        println("Communicator received ${communicator.receiveData()}")
    }
}

class RunChecker935: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}