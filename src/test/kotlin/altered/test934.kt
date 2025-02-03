/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":2,"nChannels":2,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 2 different channels
- 2 different coroutines
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
package org.example.altered.test934
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Sender {
    suspend fun sendData(channel: Channel<Int>, data: Int) {
        channel.send(data)
    }
}

class Receiver {
    suspend fun receiveData(channel: Channel<Int>): Int {
        return channel.receive()
    }
}

class Processor {
    fun process(data: Int): Int {
        return data * 2
    }
}

fun launchSender(channel: Channel<Int>, sender: Sender, data: Int) {
    GlobalScope.launch {
        sender.sendData(channel, data)
    }
}

fun launchReceiver(channel: Channel<Int>, receiver: Receiver, processor: Processor): Int {
    var result = 0
    runBlocking {
        launch {
            val receivedData = receiver.receiveData(channel)
            result = processor.process(receivedData)
        }.join()
    }
    return result
}

fun main(): Unit{
    val channel1 = Channel<Int>(1)
    val channel2 = Channel<Int>(1)
    val sender = Sender()
    val receiver = Receiver()
    val processor = Processor()
    launchSender(channel1, sender, 10)
    val result = launchReceiver(channel1, receiver, processor)
    println(result)
}

class RunChecker934: RunCheckerBase() {
    override fun block() = main()
}