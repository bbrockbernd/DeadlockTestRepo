/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":1,"nChannels":1,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 1 different channels
- 1 different coroutines
- 5 different classes

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
package org.example.altered.test459
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class DataProcessor {
    fun process(data: Int): Int {
        return data * 2
    }
}

class DataEmitter {
    suspend fun emit(channel: Channel<Int>, data: Int) {
        channel.send(data)
    }
}

class DataReceiver {
    suspend fun receive(channel: Channel<Int>): Int {
        return channel.receive()
    }
}

class DataTransformer {
    fun transform(data: Int): Int {
        return data + 3
    }
}

class DataPrinter {
    fun print(data: Int) {
        println("Processed Data: $data")
    }
}

fun prepareData(): Int {
    return 5
}

suspend fun emitData(channel: Channel<Int>, data: Int) {
    DataEmitter().emit(channel, data)
}

suspend fun receiveData(channel: Channel<Int>): Int {
    return DataReceiver().receive(channel)
}

fun processData(data: Int): Int {
    val processor = DataProcessor()
    val transformedData = DataTransformer().transform(data)
    return processor.process(transformedData)
}

fun printData(data: Int) {
    DataPrinter().print(data)
}

fun main(): Unit= runBlocking {
    val channel = Channel<Int>()
    launch {
        val data = prepareData()
        emitData(channel, data)
    }

    launch {
        val receivedData = receiveData(channel)
        val processedData = processData(receivedData)
        printData(processedData)
    }
}

class RunChecker459: RunCheckerBase() {
    override fun block() = main()
}