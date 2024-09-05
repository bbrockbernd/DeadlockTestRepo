/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":7,"nChannels":4,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 4 different channels
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
package org.example.generated.test118
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Sender(val channel: Channel<Int>) {
    suspend fun sendData(data: Int) {
        channel.send(data)
    }
}

class Receiver(val channel: Channel<Int>) {
    suspend fun receiveData(): Int {
        return channel.receive()
    }
}

class Processor(val inputChannel: Channel<Int>, val outputChannel: Channel<Int>) {
    suspend fun process() {
        val data = inputChannel.receive()
        outputChannel.send(data * 2)
    }
}

class Logger(val channel: Channel<String>) {
    suspend fun logMessage(msg: String) {
        channel.send(msg)
    }
}

class Collector(val inputChannel: Channel<Int>, val logChannel: Channel<String>) {
    suspend fun collectAndLog() {
        val data = inputChannel.receive()
        logChannel.send("Received data: $data")
    }
}

fun mainChannelSetup(): List<Channel<*>> {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()
    val channelC = Channel<Int>()
    val logChannel = Channel<String>()
    return listOf(channelA, channelB, channelC, logChannel)
}

fun CoroutineScope.startCoroutines(
    sender: Sender, 
    receiver: Receiver, 
    processor: Processor, 
    logger: Logger, 
    collector: Collector, 
    channels: List<Channel<*>>
) {
    launch {
        sender.sendData(10)
    }
    launch {
        processor.process()
    }
    launch {
        collector.collectAndLog()
    }
    launch {
        logger.logMessage("Launching logger coroutine")
    }
    launch {
        val message = logger.channel.receive()
        println(message)
    }
    launch {
        println("Processing data: ${receiver.receiveData()}")
    }
    launch {
        processor.process()
    }
}

fun main(): Unit= runBlocking {
    val channels = mainChannelSetup()
    val sender = Sender(channels[0] as Channel<Int>)
    val receiver = Receiver(channels[1] as Channel<Int>)
    val processor = Processor(channels[0] as Channel<Int>, channels[1] as Channel<Int>)
    val logger = Logger(channels[3] as Channel<String>)
    val collector = Collector(channels[2] as Channel<Int>, channels[3] as Channel<String>)
    
    startCoroutines(sender, receiver, processor, logger, collector, channels)
}