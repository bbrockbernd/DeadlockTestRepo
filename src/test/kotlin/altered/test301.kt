/* 
{"deadlock":false,"nFunctions":8,"nCoroutines":3,"nChannels":8,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
- 8 different channels
- 3 different coroutines
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
package org.example.altered.test301
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<String>()
    val channel5 = Channel<String>()
    val channel6 = Channel<Int>()
    val channel7 = Channel<Int>()
    val channel8 = Channel<String>()
    
    launch { coroutineA(channel1, channel2) }
    launch { coroutineB(channel3, channel4) }
    launch { coroutineC(channel5, channel6, channel7, channel8) }

    channel1.send(10)
    println(channel8.receive())
}

suspend fun coroutineA(channel1: Channel<Int>, channel2: Channel<Int>) {
    val data = channel1.receive()
    function1(channel2, data + 20)
}

suspend fun coroutineB(channel3: Channel<Int>, channel4: Channel<String>) {
    val data = function2(channel3, 30)
    function3(channel4, "Received: $data")
}

suspend fun coroutineC(channel5: Channel<String>, channel6: Channel<Int>, channel7: Channel<Int>, channel8: Channel<String>) {
    val data1 = function6(channel6, 5)
    val data2 = function7(channel7, 10)
    val combinedResult = function8(data1, data2)
    channel8.send(combinedResult)
}

suspend fun function1(channel: Channel<Int>, data: Int) {
    channel.send(data)
}

suspend fun function2(channel: Channel<Int>, data: Int): Int {
    channel.send(data)
    return channel.receive() + 10
}

suspend fun function3(channel: Channel<String>, message: String) {
    channel.send(message)
}

suspend fun function4(channel: Channel<String>, data: String) {
    channel.send(data)
}

suspend fun function5(channel: Channel<Int>, data: Int) {
    channel.send(data)
}

suspend fun function6(channel: Channel<Int>, data: Int): Int {
    channel.send(data)
    return channel.receive() + 15
}

suspend fun function7(channel: Channel<Int>, data: Int): Int {
    channel.send(data)
    return channel.receive() + 25
}

suspend fun function8(data1: Int, data2: Int): String {
    return "Result: ${data1 + data2}"
}

class RunChecker301: RunCheckerBase() {
    override fun block() = main()
}