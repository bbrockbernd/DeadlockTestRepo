/* 
{"deadlock":false,"nFunctions":7,"nCoroutines":8,"nChannels":2,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 2 different channels
- 8 different coroutines
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
package org.example.generated.test310
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class Processor(val inputChannel: Channel<Int>, val outputChannel: Channel<Int>) {
    suspend fun process(id: Int) {
        for (value in inputChannel) {
            outputChannel.send(value + id)
        }
    }
}

fun startCoroutines() = runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    
    launch { producer1(channel1) }
    launch { producer2(channel1) }
    launch { intermediary(channel1, channel2) }
    launch { consumer1(channel2) }
    launch { consumer2(channel2) }
    
    val processor = Processor(channel1, channel2)
    launch { processor.process(1000) }
    launch { processor.process(2000) }
    
    delay(1000)
    channel1.close()
    channel2.close()
}

suspend fun producer1(channel: Channel<Int>) {
    var i = 0
    while (i < 10) {
        channel.send(i++)
    }
}

suspend fun producer2(channel: Channel<Int>) {
    var j = 10
    while (j < 20) {
        channel.send(j++)
    }
}

suspend fun intermediary(input: Channel<Int>, output: Channel<Int>) {
    for (value in input) {
        output.send(value * 2)
    }
}

suspend fun consumer1(channel: Channel<Int>) {
    for (value in channel) {
        println("Consumer1 received: $value")
    }
}

suspend fun consumer2(channel: Channel<Int>) {
    for (value in channel) {
        println("Consumer2 received: $value")
    }
}

startCoroutines()