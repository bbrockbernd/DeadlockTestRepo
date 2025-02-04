/* 
{"deadlock":true,"nFunctions":2,"nCoroutines":7,"nChannels":2,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 2 different channels
- 7 different coroutines
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
package org.example.altered.test161
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

data class Data(val value: String)
class Producer1(private val channel: Channel<Data>) {
    suspend fun produce() {
        channel.send(Data("Data from Producer1"))
    }
}

class Producer2(private val channel: Channel<Data>) {
    suspend fun produce() {
        channel.send(Data("Data from Producer2"))
    }
}

class Consumer1(private val channel: Channel<Data>) {
    suspend fun consume() {
        val data = channel.receive()
        println("Consumer1 consumed: ${data.value}")
    }
}

class Consumer2(private val channel: Channel<Data>) {
    suspend fun consume() {
        val data = channel.receive()
        println("Consumer2 consumed: ${data.value}")
    }
}

class MiddleMan(private val inputChannel: Channel<Data>, private val outputChannel: Channel<Data>) {
    suspend fun transfer() {
        val data = inputChannel.receive()
        outputChannel.send(data)
    }
}

fun channelOperation1(channel1: Channel<Data>, channel2: Channel<Data>, middleMan: MiddleMan) = runBlocking {
    val producer1 = Producer1(channel1)
    val consumer1 = Consumer1(channel2)
    
    launch { producer1.produce() }
    launch { middleMan.transfer() }
    launch { consumer1.consume() }
}

fun channelOperation2(channel1: Channel<Data>, channel2: Channel<Data>, middleMan: MiddleMan) = runBlocking {
    val producer2 = Producer2(channel2)
    val consumer2 = Consumer2(channel1)

    launch { producer2.produce() }
    launch { middleMan.transfer() }
    launch { consumer2.consume() }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Data>()
    val channel2 = Channel<Data>()
    val middleMan = MiddleMan(channel1, channel2)
    
    launch { channelOperation1(channel1, channel2, middleMan) }
    launch { channelOperation2(channel1, channel2, middleMan) }
}

class RunChecker161: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}