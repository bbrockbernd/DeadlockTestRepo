/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":1,"nChannels":5,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 5 different channels
- 1 different coroutines
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
package org.example.altered.test654
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()

    coroutineScope {
        launch { function1(channel1, channel2) }
        launch { function2(channel2, channel3) }
        launch { function3(channel3, channel4) }
        launch { function4(channel4, channel5) }
        launch { function5(channel5, channel1) }
    }
}

suspend fun function1(channel1: Channel<Int>, channel2: Channel<Int>) {
    channel1.send(1)
    channel2.receive()
}

suspend fun function2(channel2: Channel<Int>, channel3: Channel<Int>) {
    channel2.send(2)
    channel3.receive()
}

suspend fun function3(channel3: Channel<Int>, channel4: Channel<Int>) {
    channel3.send(3)
    channel4.receive()
}

suspend fun function4(channel4: Channel<Int>, channel5: Channel<Int>) {
    channel4.send(4)
    channel5.receive()
}

suspend fun function5(channel5: Channel<Int>, channel1: Channel<Int>) {
    channel5.send(5)
    channel1.receive()
}

class RunChecker654: RunCheckerBase() {
    override fun block() = main()
}