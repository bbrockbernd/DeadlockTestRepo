/* 
{"deadlock":true,"nFunctions":8,"nCoroutines":1,"nChannels":1,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
- 1 different channels
- 1 different coroutines
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
package org.example.altered.test489
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer {
    private val channel = Channel<Int>()

    fun produce() = runBlocking {
        launch {
            sendData(5)
        }
    }

    suspend fun sendData(element: Int) {
        sendToChannel(channel, element)
    }

    suspend fun sendToChannel(channel: Channel<Int>, element: Int) {
        channel.send(element)
    }

    fun getChannel(): Channel<Int> = channel
}

class Consumer {
    @OptIn(DelicateCoroutinesApi::class)
    fun consume(channel: Channel<Int>) = runBlocking {
        GlobalScope.launch {
            receiveData(channel)
        }
    }

    suspend fun receiveData(channel: Channel<Int>) {
        readFromChannel(channel)
    }

    suspend fun readFromChannel(channel: Channel<Int>) {
        val data = channel.receive()
        processData(data)
    }

    suspend fun processData(data: Int) = delay(1000)
}

fun createProducer(): Producer {
    return Producer()
}

fun createConsumer(): Consumer {
    return Consumer()
}

fun main(): Unit {
    val producer = createProducer()
    val consumer = createConsumer()
    
    producer.produce()
    consumer.consume(producer.getChannel())
}

class RunChecker489: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}