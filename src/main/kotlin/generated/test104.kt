/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":5,"nChannels":2,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 2 different channels
- 5 different coroutines
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
package org.example.generated.test104
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class DataProcessor(val channel1: Channel<Int>, val channel2: Channel<Int>) {
    suspend fun processData() {
        for (i in 1..5) {
            val data1 = channel1.receive()
            val data2 = channel2.receive()
            println("Processing data: $data1 and $data2")
        }
    }
}

fun sendData(channel1: Channel<Int>, channel2: Channel<Int>) = runBlocking {
    launch { 
        for (i in 1..5) {
            channel1.send(i)
            println("Sent $i to channel1")
        }
    }
    launch { 
        for (i in 6..10) {
            channel2.send(i)
            println("Sent $i to channel2")
        }
    }
}

suspend fun receiveData(channel: Channel<Int>, name: String) {
    coroutineScope {
        launch {
            for (i in 1..5) {
                val data = channel.receive()
                println("$name received: $data")
            }
        }
    }
}

suspend fun operation1(channel: Channel<Int>, name: String) {
    coroutineScope {
        launch {
            for (i in 11..15) {
                channel.send(i)
                println("$name sent: $i")
            }
        }
    }
}

suspend fun operation2(channel: Channel<Int>, name: String) {
    coroutineScope {
        launch {
            for (i in 16..20) {
                val data = channel.receive()
                println("$name received: $data")
            }
        }
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()

    val processor = DataProcessor(channel1, channel2)

    launch { sendData(channel1, channel2) }
    launch { processor.processData() }
    launch { receiveData(channel1, "Receiver1") }
    launch { operation1(channel2, "Operation1") }
    launch { operation2(channel1, "Operation2") }
}