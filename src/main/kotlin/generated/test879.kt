/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":1,"nChannels":3,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 3 different channels
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
package org.example.generated.test879
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class Producer(private val channel: Channel<Int>) {
    suspend fun produce() {
        repeat(10) {
            channel.send(it)
        }
        channel.close()
    }
}

class Consumer(private val channel: Channel<Int>) {
    suspend fun consume(action: (Int) -> Unit) {
        for (value in channel) {
            action(value)
        }
    }
}

fun createChannels(): Triple<Channel<Int>, Channel<Int>, Channel<Int>> {
    return Triple(Channel(), Channel(), Channel())
}

suspend fun sendToThirdChannel(sourceChannel: Channel<Int>, destChannel: Channel<Int>) {
    for (value in sourceChannel) {
        destChannel.send(value * 2)
    }
    destChannel.close()
}

fun CoroutineScope.runScenario() {
    val (chan1, chan2, chan3) = createChannels()

    launch {
        val producer = Producer(chan1)
        val consumer = Consumer(chan1)
        producer.produce()
        consumer.consume { println("Consume from chan1: $it") }
    }

    launch {
        sendToThirdChannel(chan1, chan2)
    }

    launch {
        val consumer2 = Consumer(chan2)
        consumer2.consume { println("Consume from chan2: $it") }
    }

    launch {
        sendToThirdChannel(chan2, chan3)
    }

    launch {
        val consumer3 = Consumer(chan3)
        consumer3.consume { println("Consume from chan3: $it") }
    }
}

fun main(): Unit= runBlocking {
    runScenario()
}