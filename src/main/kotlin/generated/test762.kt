/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":2,"nChannels":3,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 3 different channels
- 2 different coroutines
- 0 different classes

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
package org.example.generated.test762
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()

    launch { coroutine1(channel1, channel2) }
    launch { coroutine2(channel2, channel3) }

    runBlockingFunction(channel3, channel1)
}

suspend fun coroutine1(channel1: Channel<Int>, channel2: Channel<Int>) {
    receiveFromChannel1(channel1)
    sendToChannel2(channel2)
}

suspend fun receiveFromChannel1(channel1: Channel<Int>) {
    val received = channel1.receive()
    processReceivedData(received)
}

suspend fun sendToChannel2(channel2: Channel<Int>) {
    val dataToSend = generateData()
    channel2.send(dataToSend)
}

suspend fun coroutine2(channel2: Channel<Int>, channel3: Channel<Int>) {
    receiveFromChannel2(channel2)
    sendToChannel3(channel3)
}

suspend fun receiveFromChannel2(channel2: Channel<Int>) {
    val received = channel2.receive()
    processReceivedData(received)
}

suspend fun sendToChannel3(channel3: Channel<Int>) {
    val dataToSend = generateData()
    channel3.send(dataToSend)
}

suspend fun runBlockingFunction(channel3: Channel<Int>, channel1: Channel<Int>) {
    receiveFromChannel3(channel3)
    sendToChannel1(channel1)
}

suspend fun receiveFromChannel3(channel3: Channel<Int>) {
    val received = channel3.receive()
    processReceivedData(received)
}

suspend fun sendToChannel1(channel1: Channel<Int>) {
    val dataToSend = generateData()
    channel1.send(dataToSend)
}

fun processReceivedData(data: Int) {
    println("Processed data: $data")
}

fun generateData(): Int {
    return 42
}