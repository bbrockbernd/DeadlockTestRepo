/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":6,"nChannels":2,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 2 different channels
- 6 different coroutines
- 2 different classes

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
package org.example.altered.test115
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class DataSender(private val channel: Channel<Int>) {
    suspend fun sendData(data: Int) {
        channel.send(data)
    }
}

class DataReceiver(private val channel: Channel<Int>) {
    suspend fun receiveData(): Int {
        return channel.receive()
    }
}

fun produceData(dataSender: DataSender, data: Int) = runBlocking {
    launch {
        repeat(3) {
            dataSender.sendData(data + it)
        }
    }
}

fun processData(dataReceiver: DataReceiver) = runBlocking {
    launch {
        repeat(3) {
            val data = dataReceiver.receiveData()
            println("Processed data: $data")
        }
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    
    val sender1 = DataSender(channel1)
    val receiver1 = DataReceiver(channel1)

    val sender2 = DataSender(channel2)
    val receiver2 = DataReceiver(channel2)
    
    // Launch coroutines for producing and processing data
    launch {
        produceData(sender1, 10)
        processData(receiver1)
    }
    
    launch {
        produceData(sender2, 100)
        processData(receiver2)
    }
    
    coroutineScope {
        launch {
            produceData(sender1, 20)
            processData(receiver1)
        }
        
        launch {
            produceData(sender2, 200)
            processData(receiver2)
        }
    }
    
    delay(1000L) // Allow some time for all coroutines to finish
}

class RunChecker115: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}