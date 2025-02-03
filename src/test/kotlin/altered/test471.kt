/* 
{"deadlock":false,"nFunctions":6,"nCoroutines":1,"nChannels":3,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 3 different channels
- 1 different coroutines
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
package org.example.altered.test471
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class DataProducer(val channel: Channel<Int>) {
    suspend fun produceData() {
        for (i in 1..5) {
            channel.send(i)
        }
        channel.close()
    }
}

class DataConsumer(val channel: Channel<Int>) {
    suspend fun consumeData() {
        for (i in channel) {
            println("Consumed: $i")
        }
    }
}

class Controller(val producer: DataProducer, val consumer: DataConsumer) {
    suspend fun control() {
        coroutineScope {
            launch { producer.produceData() }
            launch { consumer.consumeData() }
        }
    }
}

class DataBroadcaster(val channels: List<Channel<Int>>) {
    suspend fun broadcast() {
        for (channel in channels) {
            channel.send(42)
            channel.close()
        }
    }
}

class DataReceiver(val channel: Channel<Int>) {
    suspend fun receiveAndPrint() {
        for (i in channel) {
            println("Received: $i")
        }
    }
}

class ChannelOperator(val channel: Channel<Int>) {
    suspend fun operate() {
        val received = channel.receive()
        println("Operated: $received")
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()

    val producer = DataProducer(channel1)
    val consumer = DataConsumer(channel1)

    val controller = Controller(producer, consumer)
    val broadcaster = DataBroadcaster(listOf(channel2, channel3))
    
    val receiver1 = DataReceiver(channel2)
    val receiver2 = DataReceiver(channel3)
    
    val operator = ChannelOperator(channel2)

    launch {
        controller.control()
    }
    launch {
        broadcaster.broadcast()
    }
    launch {
        receiver1.receiveAndPrint()
    }
    launch {
        receiver2.receiveAndPrint()
    }
    launch {
        operator.operate()
    }
}

class RunChecker471: RunCheckerBase() {
    override fun block() = main()
}