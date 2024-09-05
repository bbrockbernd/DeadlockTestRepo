/* 
{"deadlock":false,"nFunctions":8,"nCoroutines":5,"nChannels":2,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
- 2 different channels
- 5 different coroutines
- 4 different classes

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
package org.example.generated.test63
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Writer(val channel: Channel<Int>) {
    suspend fun writeData() {
        for (i in 1..5) {
            channel.send(i)
        }
    }
}

class Reader(val channel: Channel<Int>) {
    suspend fun readData() {
        for (i in 1..5) {
            println("Received: ${channel.receive()}")
        }
    }
}

class Processor1(val channel: Channel<Int>, val channel2: Channel<Int>) {
    suspend fun process() {
        for (i in 1..5) {
            val data = channel.receive()
            channel2.send(data * 2)
        }
    }
}

class Processor2(val channel: Channel<Int>, val channel2: Channel<Int>) {
    suspend fun process() {
        for (i in 1..5) {
            val data = channel2.receive()
            channel.send(data - 1)
        }
    }
}

suspend fun writerFunction(writer: Writer) {
    writer.writeData()
}

suspend fun readerFunction(reader: Reader) {
    reader.readData()
}

suspend fun processor1Function(processor1: Processor1) {
    processor1.process()
}

suspend fun processor2Function(processor2: Processor2) {
    processor2.process()
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()

    val writer = Writer(channel1)
    val reader = Reader(channel1)
    val processor1 = Processor1(channel1, channel2)
    val processor2 = Processor2(channel2, channel1)

    launch { writerFunction(writer) }
    launch { processor1Function(processor1) }
    launch { processor2Function(processor2) }
    launch { writerFunction(writer) }
    launch { readerFunction(reader) }
}