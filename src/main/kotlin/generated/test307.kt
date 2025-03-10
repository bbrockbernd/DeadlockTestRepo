/* 
{"deadlock":false,"nFunctions":6,"nCoroutines":4,"nChannels":7,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 7 different channels
- 4 different coroutines
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
package org.example.generated.test307
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()
    val channel6 = Channel<Int>()
    val channel7 = Channel<Int>()

    launch { function1(channel1, channel2, channel3) }
    launch { function2(channel4, channel5) }
    launch { function3(channel6) }
    launch { function4(channel1, channel2, channel3, channel4, channel5, channel6, channel7) }

    function5(channel7)
    function6(channel7)
}

suspend fun function1(channel1: Channel<Int>, channel2: Channel<Int>, channel3: Channel<Int>) {
    channel1.send(1)
    channel2.send(channel1.receive())
    channel3.send(channel2.receive())
}

suspend fun function2(channel4: Channel<Int>, channel5: Channel<Int>) {
    channel4.send(2)
    channel5.send(channel4.receive())
}

suspend fun function3(channel6: Channel<Int>) {
    delay(1000)
    channel6.send(3)
}

suspend fun function4(
    channel1: Channel<Int>, channel2: Channel<Int>, channel3: Channel<Int>, 
    channel4: Channel<Int>, channel5: Channel<Int>, channel6: Channel<Int>, 
    channel7: Channel<Int>
) {
    channel7.send(channel3.receive() + channel5.receive() + channel6.receive())
}

suspend fun function5(channel7: Channel<Int>) {
    delay(500)
    val result = channel7.receive()
    println("Result from function5: $result")
}

suspend fun function6(channel7: Channel<Int>) {
    delay(1500)
    println("In function6, channel7 is ready: ${channel7.isEmpty}")
}