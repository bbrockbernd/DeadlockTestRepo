/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":5,"nChannels":5,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 5 different channels
- 5 different coroutines
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
package org.example.generated.test371
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(val channel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            channel.send(i)
        }
        channel.close()
    }
}

class Consumer(val channel: Channel<Int>) {
    suspend fun consume() {
        for (i in channel) {
            println("Consumed $i")
        }
    }
}

class Supervisor(val pChannel: Channel<Int>, val cChannel: Channel<Int>) {
    suspend fun supervise() {
        for (i in pChannel) {
            cChannel.send(i * 2)
        }
        cChannel.close()
    }
}

fun createChannels(): List<Channel<Int>> {
    return listOf(Channel(), Channel(), Channel(), Channel(), Channel())
}

fun setupCoroutines(channels: List<Channel<Int>>) = runBlocking {
    val producer = Producer(channels[0])
    val consumer = Consumer(channels[4])
    val supervisor1 = Supervisor(channels[0], channels[1])
    val supervisor2 = Supervisor(channels[1], channels[2])
    val supervisor3 = Supervisor(channels[2], channels[3])

    coroutineScope {
        launch { producer.produce() } 
        launch { supervisor1.supervise() } 
        launch { supervisor2.supervise() } 
        launch { supervisor3.supervise() }
        launch { consumer.consume() }
    }
}

fun main(): Unit{
    val channels = createChannels()
    setupCoroutines(channels)
}