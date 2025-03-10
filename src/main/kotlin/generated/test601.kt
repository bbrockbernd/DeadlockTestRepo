/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":4,"nChannels":1,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 1 different channels
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
package org.example.generated.test601
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Processor(val channel: Channel<Int>) {
    suspend fun sendValue(value: Int) {
        channel.send(value)
    }

    suspend fun receiveValue(): Int {
        return channel.receive()
    }
}

fun initChannel(): Channel<Int> {
    return Channel()
}

suspend fun process1(processor: Processor) {
    processor.sendValue(1)
}

suspend fun process2(processor: Processor) {
    val value = processor.receiveValue()
    if (value == 1) processor.sendValue(2)
}

suspend fun process3(processor: Processor) {
    val value = processor.receiveValue()
    if (value == 2) processor.sendValue(3)
}

suspend fun process4(processor: Processor) {
    val value = processor.receiveValue()
    if (value == 3) processor.sendValue(4)
}

fun main(): Unit= runBlocking {
    val channel = initChannel()
    val processor = Processor(channel)

    launch { process1(processor) }
    launch { process2(processor) }
    launch { process3(processor) }
    launch { process4(processor) }

    delay(1000)  // Give some time for all processes to complete
}