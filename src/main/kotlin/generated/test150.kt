/* 
{"deadlock":false,"nFunctions":7,"nCoroutines":6,"nChannels":4,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 4 different channels
- 6 different coroutines
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
package org.example.generated.test150
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Processor(val inputChannel: Channel<Int>, val outputChannel: Channel<Int>)

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()

    val processor1 = Processor(channel1, channel2)
    val processor2 = Processor(channel2, channel3)
    val processor3 = Processor(channel3, channel4)
    val processor4 = Processor(channel4, channel1)

    launch { function1(processor1) }
    launch { function2(processor2) }
    launch { function3(processor3) }
    launch { function4(processor4) }
    launch { function5(channel1) }
    launch { function6(channel4) }
}

suspend fun function1(processor: Processor) {
    for (i in 1..100) {
        processor.inputChannel.send(i)
        val received = processor.outputChannel.receive()
        println("Function1 received: $received")
    }
}

suspend fun function2(processor: Processor) {
    for (i in 1..100) {
        val received = processor.inputChannel.receive()
        println("Function2 received: $received")
        processor.outputChannel.send(received + 2)
    }
}

suspend fun function3(processor: Processor) {
    for (i in 1..100) {
        val received = processor.inputChannel.receive()
        println("Function3 received: $received")
        processor.outputChannel.send(received * 3)
    }
}

suspend fun function4(processor: Processor) {
    for (i in 1..100) {
        val received = processor.inputChannel.receive()
        println("Function4 received: $received")
        processor.outputChannel.send(received - 4)
    }
}

suspend fun function5(channel: Channel<Int>) {
    for (i in 1..100) {
        val received = channel.receive()
        println("Function5 received: $received")
    }
}

suspend fun function6(channel: Channel<Int>) {
    for (i in 1..100) {
        channel.send(i * 5)
        println("Function6 sent: ${i * 5}")
    }
}