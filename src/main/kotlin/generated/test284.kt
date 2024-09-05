/* 
{"deadlock":false,"nFunctions":7,"nCoroutines":5,"nChannels":8,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 8 different channels
- 5 different coroutines
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
package org.example.generated.test284
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class Producer(private val outChannel: SendChannel<Int>) {

    suspend fun produce() {
        for (i in 1..5) {
            outChannel.send(i)
        }
        outChannel.close()
    }
}

class Consumer(private val inChannel: ReceiveChannel<Int>, private val outChannel: SendChannel<Int>) {

    suspend fun consumeAndProduce() {
        for (value in inChannel) {
            outChannel.send(value * 2)
        }
        outChannel.close()
    }
}

fun createChannels(): List<Channel<Int>> {
    val channels = mutableListOf<Channel<Int>>()
    for (i in 1..8) {
        channels.add(Channel())
    }
    return channels
}

suspend fun producerFunction(producer: Producer) {
    producer.produce()
}

suspend fun consumerFunction(consumer: Consumer) {
    consumer.consumeAndProduce()
}

fun additionalFunction1(channel: ReceiveChannel<Int>, outChannel: SendChannel<Int>) = runBlocking {
    for (value in channel) {
        outChannel.send(value + 1)
    }
    outChannel.close()
}

fun additionalFunction2(channel: ReceiveChannel<Int>, outChannel: SendChannel<Int>) = runBlocking {
    for (value in channel) {
        outChannel.send(value - 1)
    }
    outChannel.close()
}

fun additionalFunction3(channel: ReceiveChannel<Int>, outChannel: SendChannel<Int>) = runBlocking {
    for (value in channel) {
        outChannel.send(value * value)
    }
    outChannel.close()
}

fun main(): Unit= runBlocking {
    val channels = createChannels()

    val producer = Producer(channels[0])
    val consumer1 = Consumer(channels[0], channels[1])
    val consumer2 = Consumer(channels[1], channels[2])
    val consumer3 = Consumer(channels[2], channels[3])
    val consumer4 = Consumer(channels[3], channels[4])
    val consumer5 = Consumer(channels[4], channels[5])

    launch { producerFunction(producer) }
    launch { consumerFunction(consumer1) }
    launch { consumerFunction(consumer2) }
    launch { consumerFunction(consumer3) }
    launch { consumerFunction(consumer4) }
    
    additionalFunction1(channels[5], channels[6])
    additionalFunction2(channels[6], channels[7])
    additionalFunction3(channels[7], Channel()) // Last Channel [7] does not send to another channel
}