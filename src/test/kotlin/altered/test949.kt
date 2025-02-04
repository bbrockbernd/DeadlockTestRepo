/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":5,"nChannels":5,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
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
package org.example.altered.test949
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class Sensor(val id: Int, private val channel: Channel<Int>) {
    suspend fun produceData() {
        for (i in 1..5) {
            channel.send(id * 100 + i)
        }
    }
}

class Processor(private val inputChannel: Channel<Int>, private val outputChannel: Channel<Boolean>) {
    suspend fun processData() {
        for (value in 1..5) {
            val received = inputChannel.receive()
            outputChannel.send(received % 2 == 0)
        }
    }
}

class Aggregator(private val channel: Channel<Boolean>) {
    suspend fun aggregateData() {
        var trueCount = 0
        var falseCount = 0
        for (i in 1..5) {
            if (channel.receive()) {
                trueCount++
            } else {
                falseCount++
            }
        }
        println("True: $trueCount, False: $falseCount")
    }
}

suspend fun sensorCoroutines(channels: List<Channel<Int>>) = coroutineScope {
    for (i in channels.indices) {
        val sensor = Sensor(i + 1, channels[i])
        launch { sensor.produceData() }
    }
}

suspend fun processorCoroutines(inputChannels: List<Channel<Int>>, outputChannel: Channel<Boolean>) = coroutineScope {
    for (inputChannel in inputChannels) {
        val processor = Processor(inputChannel, outputChannel)
        launch { processor.processData() }
    }
}

suspend fun aggregatorCoroutine(channel: Channel<Boolean>) = coroutineScope {
    val aggregator = Aggregator(channel)
    launch { aggregator.aggregateData() }
}

suspend fun startCoroutines() = coroutineScope {
    val channels = listOf(Channel<Int>(), Channel<Int>(), Channel<Int>(), Channel<Int>())
    val boolChannel = Channel<Boolean>()
    launch { sensorCoroutines(channels) }
    launch { processorCoroutines(channels, boolChannel) }
    launch { aggregatorCoroutine(boolChannel) }
}

fun main(): Unit= runBlocking {
    startCoroutines()
}

class RunChecker949: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}