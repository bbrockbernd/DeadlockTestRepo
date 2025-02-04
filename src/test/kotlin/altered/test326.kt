/* 
{"deadlock":true,"nFunctions":6,"nCoroutines":6,"nChannels":7,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 7 different channels
- 6 different coroutines
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
package org.example.altered.test326
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class DataChannel(val id: Int, val channel: Channel<Int>)
class ProcessingUnit(val id: Int, val inputChannel: Channel<Int>, val outputChannel: Channel<Int>)
class Pipeline(val units: List<ProcessingUnit>, val finalChannel: Channel<Int>)

fun generateData(channel: DataChannel) = runBlocking {
    launch {
        repeat(10) {
            channel.channel.send(it)
        }
    }
}

fun processFirstUnit(unit: ProcessingUnit) = runBlocking {
    launch {
        repeat(10) {
            val data = unit.inputChannel.receive()
            unit.outputChannel.send(data * 2)
        }
    }
}

fun processSecondUnit(unit: ProcessingUnit) = runBlocking {
    launch {
        repeat(10) {
            val data = unit.inputChannel.receive()
            unit.outputChannel.send(data + 1)
        }
    }
}

fun buildPipeline(): Pipeline {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()
    val finalChannel = Channel<Int>()
    
    val unit1 = ProcessingUnit(1, channel1, channel2)
    val unit2 = ProcessingUnit(2, channel2, channel3)
    val unit3 = ProcessingUnit(3, channel3, channel4)
    val unit4 = ProcessingUnit(4, channel4, channel5)
    val unit5 = ProcessingUnit(5, channel5, finalChannel)
    
    return Pipeline(listOf(unit1, unit2, unit3, unit4, unit5), finalChannel)
}

fun runPipeline(pipeline: Pipeline) = runBlocking {
    pipeline.units.forEach { unit ->
        launch {
            when (unit.id) {
                1 -> processFirstUnit(unit)
                2 -> processSecondUnit(unit)
                3 -> processFirstUnit(unit)
                4 -> processSecondUnit(unit)
                5 -> processFirstUnit(unit)
            }
        }
    }
}

fun main(): Unit = runBlocking {
    val dataChannel = DataChannel(0, Channel<Int>())
    generateData(dataChannel)
    
    val pipeline = buildPipeline()
    runPipeline(pipeline)
    
    launch {
        repeat(10) {
            dataChannel.channel.send(it)
        }
    }
    
    launch {
        repeat(10) {
            println(pipeline.finalChannel.receive())
        }
    }
}

class RunChecker326: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}