/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":6,"nChannels":4,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 4 different channels
- 6 different coroutines
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
package org.example.altered.test306
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class ProducerChannel(val channel: Channel<Int>) {
    suspend fun produce(values: List<Int>) {
        for (value in values) {
            channel.send(value)
        }
        channel.close()
    }
}

class ConsumerChannel(val channel: Channel<Int>) {
    suspend fun consume(resultChannel: Channel<Int>) {
        for (value in channel) {
            resultChannel.send(value * 2)
        }
        resultChannel.close()
    }
}

fun startProcessing() {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()

    val producer = ProducerChannel(channel1)
    val consumer1 = ConsumerChannel(channel1)
    val consumer2 = ConsumerChannel(channel2)

    runBlocking {
        launch { producer.produce(listOf(1, 2, 3, 4)) }
        launch { consumer1.consume(channel2) }
        launch { consumer2.consume(channel3) }

        launch {
            for (value in channel3) {
                channel4.send(value + 1)
            }
            channel4.close()
        }

        launch {
            for (value in channel4) {
                println(value)
            }
        }
    }
}

fun main(): Unit{
    startProcessing()
}

class RunChecker306: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}