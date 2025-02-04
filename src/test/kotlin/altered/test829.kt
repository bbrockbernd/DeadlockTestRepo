/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":1,"nChannels":2,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 2 different channels
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
package org.example.altered.test829
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class Producer {
    suspend fun produce(channel: Channel<Int>) {
        for (i in 1..5) {
            channel.send(i)
        }
        channel.close()
    }
}

class Consumer {
    suspend fun consume(channel: Channel<Int>) {
        for (i in 1..5) {
            println("Received: ${channel.receive()}")
        }
    }
}

fun prepareProducer(channel: Channel<Int>) = runBlocking {
    val producer = Producer()
    launch { producer.produce(channel) }
}

fun prepareConsumer(channel: Channel<Int>) = runBlocking {
    val consumer = Consumer()
    launch { consumer.consume(channel) }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    
    prepareProducer(channel1)
    prepareConsumer(channel1)

    coroutineScope {
        launch {
            for (i in 6..10) {
                channel2.send(i)
                println("Sent to channel2: $i")
            }
            channel2.close()
        }
        
        launch {
            for (i in 6..10) {
                println("Received from channel2: ${channel2.receive()}")
            }
        }
    }
}

class RunChecker829: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}