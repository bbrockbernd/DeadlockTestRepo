/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":2,"nChannels":5,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 5 different channels
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
package org.example.altered.test572
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Sender(private val channel: Channel<Int>) {
    suspend fun sendValue(value: Int) {
        channel.send(value)
    }
}

class Receiver(private val channel: Channel<Int>) {
    suspend fun receiveValue(): Int {
        return channel.receive()
    }
}

class Processor(
    private val sender: Sender,
    private val receiver: Receiver,
    private val channel: Channel<Int>
) {
    suspend fun processValues() {
        val value = receiver.receiveValue() + 1
        sender.sendValue(value)
    }
}

fun launchSender(channel: Channel<Int>) = CoroutineScope(Dispatchers.Default).launch {
    val sender = Sender(channel)
    repeat(5) {
        sender.sendValue(it)
    }
}

fun launchReceiver(channel: Channel<Int>) = CoroutineScope(Dispatchers.Default).launch {
    val receiver = Receiver(channel)
    repeat(5) {
        receiver.receiveValue()
    }
}

fun testSample() = runBlocking {
    val channel1 = Channel<Int>(Channel.UNLIMITED)
    val channel2 = Channel<Int>(Channel.UNLIMITED)
    val channel3 = Channel<Int>(Channel.UNLIMITED)
    val channel4 = Channel<Int>(Channel.UNLIMITED)
    val channel5 = Channel<Int>(Channel.UNLIMITED)

    val sender1 = Sender(channel1)
    val receiver1 = Receiver(channel2)
    val processor1 = Processor(sender1, receiver1, channel3)

    launchSender(channel1)
    launchReceiver(channel2)

    val sender2 = Sender(channel4)
    val receiver2 = Receiver(channel5)
    val processor2 = Processor(sender2, receiver2, channel3)

    coroutineScope {
        launch {
            processor1.processValues()
            processor2.processValues()
        }
    }
}

testSample()

class RunChecker572: RunCheckerBase() {
    override fun block() = main()
}