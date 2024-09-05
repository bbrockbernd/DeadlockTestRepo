/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":1,"nChannels":5,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 5 different channels
- 1 different coroutines
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
package org.example.generated.test807
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

data class Data(val value: Int)

class Producer1(private val channel: Channel<Data>) {
    suspend fun produce() {
        repeat(5) {
            channel.send(Data(it))
        }
    }
}

class Producer2(private val channel: Channel<Data>) {
    suspend fun produce() {
        repeat(5) {
            channel.send(Data(it + 5))
        }
    }
}

class Consumer(private val channel1: Channel<Data>, private val channel2: Channel<Data>) {
    suspend fun consume() {
        repeat(5) {
            val data1 = channel1.receive()
            val data2 = channel2.receive()
            process(data1, data2)
        }
    }

    private suspend fun process(data1: Data, data2: Data) {
        println("Processed ${data1.value} and ${data2.value}")
    }
}

fun mainFunction() {
    runBlocking {
        val channel1 = Channel<Data>()
        val channel2 = Channel<Data>()
        val channel3 = Channel<Data>()
        val channel4 = Channel<Data>()
        val channel5 = Channel<Data>()

        launch { 
            Producer1(channel1).produce()
        }
        
        launch { 
            Producer2(channel2).produce()
        }
        
        launch { 
            Consumer(channel1, channel2).consume()
        }

        launch { 
            intermediaryFunction(channel3, channel4)
        }
        
        launch { 
            finalFunction(channel4, channel5)
        }
    }
}

suspend fun intermediaryFunction(channel3: Channel<Data>, channel4: Channel<Data>) {
    val receivedData = channel3.receive()
    channel4.send(receivedData)
}

suspend fun finalFunction(channel4: Channel<Data>, channel5: Channel<Data>) {
    val receivedData = channel4.receive()
    println("Final data: ${receivedData.value}")
    channel5.send(receivedData)
}

mainFunction()