/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":3,"nChannels":2,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 2 different channels
- 3 different coroutines
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
package org.example.altered.test854
import org.example.altered.test854.RunChecker854.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class MessageProducer(private val channel: Channel<String>) {
    suspend fun produceMessages() {
        channel.send("Message 1")
        channel.send("Message 2")
    }
}

class MessageConsumer(private val channel: Channel<String>) {
    suspend fun consumeMessages() {
        println(channel.receive())
        println(channel.receive())
    }
}

fun launchProducer(coroutineScope: CoroutineScope, producer: MessageProducer) {
    coroutineScope.launch(pool) {
        producer.produceMessages()
    }
}

fun launchConsumer(coroutineScope: CoroutineScope, consumer: MessageConsumer) {
    coroutineScope.launch(pool) {
        consumer.consumeMessages()
    }
}

fun main(): Unit= runBlocking(pool) {
    val channel1 = Channel<String>()
    val channel2 = Channel<String>()
    
    val producer1 = MessageProducer(channel1)
    val consumer1 = MessageConsumer(channel1)
    val producer2 = MessageProducer(channel2)
    
    launchProducer(this, producer1)
    launchConsumer(this, consumer1)
    launchProducer(this, producer2)
    
    delay(1000) // To ensure the messages are consumed before the main function exit
}

class RunChecker854: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}