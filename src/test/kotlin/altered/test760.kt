/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":4,"nChannels":2,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 2 different channels
- 4 different coroutines
- 1 different classes

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
package org.example.altered.test760
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Processor(val inputChannel: Channel<Int>, val outputChannel: Channel<Int>) {
    suspend fun process() {
        for (data in inputChannel) {
            outputChannel.send(data * 2)
        }
    }
}

fun CoroutineScope.produceData(channel: Channel<Int>, data: Int) {
    launch {
        channel.send(data)
        channel.close()
    }
}

fun CoroutineScope.consumeData(channel: Channel<Int>) {
    launch {
        for (data in channel) {
            println("Consumed: $data")
        }
    }
}

fun main(): Unit= runBlocking {
    val inputChannel = Channel<Int>(1)
    val outputChannel = Channel<Int>(1)

    val processor = Processor(inputChannel, outputChannel)

    launch { processor.process() }
    produceData(channel = inputChannel, data = 10)
    consumeData(channel = outputChannel)
}

class RunChecker760: RunCheckerBase() {
    override fun block() = main()
}