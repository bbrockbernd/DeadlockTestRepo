/* 
{"deadlock":true,"nFunctions":7,"nCoroutines":4,"nChannels":5,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 5 different channels
- 4 different coroutines
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
package org.example.generated.test129
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Processor(val id: Int, val input: Channel<Int>, val output: Channel<Int>) {
    suspend fun process() {
        val value = input.receive()
        println("Processor $id received $value")
        output.send(value + id)
    }
}

class Worker(val id: Int, private val processor: Processor) {
    fun start() = GlobalScope.launch {
        while (true) {
            processor.process()
        }
    }
}

class Coordinator(private val c1: Channel<Int>, private val c2: Channel<Int>) {
    suspend fun coordinate() {
        val value1 = c1.receive()
        println("Coordinator received $value1 on channel 1")
        val value2 = c2.receive()
        println("Coordinator received $value2 on channel 2")
    }
}

val channel1 = Channel<Int>()
val channel2 = Channel<Int>()
val channel3 = Channel<Int>()
val channel4 = Channel<Int>()
val channel5 = Channel<Int>()

fun createProcessor(id: Int, input: Channel<Int>, output: Channel<Int>): Processor {
    return Processor(id, input, output)
}

suspend fun sendData(channel: Channel<Int>, data: Int) {
    channel.send(data)
}

fun startCoordinator(c1: Channel<Int>, c2: Channel<Int>) = GlobalScope.launch {
    val coordinator = Coordinator(c1, c2)
    coordinator.coordinate()
}

fun main(): Unit= runBlocking {
    val processor1 = createProcessor(1, channel1, channel2)
    val processor2 = createProcessor(2, channel2, channel3)
    val processor3 = createProcessor(3, channel3, channel4)

    val worker1 = Worker(1, processor1)
    val worker2 = Worker(2, processor2)
    val worker3 = Worker(3, processor3)

    worker1.start()
    worker2.start()
    worker3.start()

    startCoordinator(channel4, channel5)

    coroutineScope {
        launch {
            sendData(channel1, 42)
        }
    }
}