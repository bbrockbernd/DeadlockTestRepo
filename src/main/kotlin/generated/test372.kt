/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":4,"nChannels":6,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 6 different channels
- 4 different coroutines
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
package org.example.generated.test372
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class DataA(val data: String)
class DataB(val data: String)
class DataC(val data: String)
class DataD(val data: String)

val channel1 = Channel<DataA>()
val channel2 = Channel<DataB>()
val channel3 = Channel<DataC>()
val channel4 = Channel<DataD>()
val channel5 = Channel<DataA>()
val channel6 = Channel<DataB>()

fun functionOne() {
    runBlocking {
        launch {
            val data = DataA("A")
            channel1.send(data)
            val receivedData = channel2.receive()
            channel3.send(DataC(receivedData.data))
        }
    }
}

fun functionTwo() {
    runBlocking {
        launch {
            val data = channel1.receive()
            channel5.send(data)
            val receivedData = channel3.receive()
            channel4.send(DataD(receivedData.data))
        }
    }
}

fun functionThree() {
    runBlocking {
        launch {
            val data = channel5.receive()
            channel2.send(DataB(data.data))
            val receivedData = channel4.receive()
            println("Received in functionThree: ${receivedData.data}")
        }
    }
}

fun functionFour() {
    runBlocking {
        launch {
            val data = DataB("B")
            channel6.send(data)
            val receivedData = channel6.receive()
            println("Received in functionFour: ${receivedData.data}")
        }
    }
}

fun main(): Unit{
    functionOne()
    functionTwo()
    functionThree()
    functionFour()
}