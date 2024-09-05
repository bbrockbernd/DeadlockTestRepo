/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":1,"nChannels":3,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 3 different channels
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
package org.example.generated.test781
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

fun function1(channel1: SendChannel<Int>) {
    GlobalScope.launch {
        repeat(5) {
            channel1.send(it)
        }
        channel1.close()
    }
}

fun function2(channel1: ReceiveChannel<Int>, channel2: SendChannel<Int>) {
    GlobalScope.launch {
        for (value in channel1) {
            channel2.send(value * 2)
        }
        channel2.close()
    }
}

fun function3(channel2: ReceiveChannel<Int>, channel3: SendChannel<Int>) {
    GlobalScope.launch {
        for (value in channel2) {
            channel3.send(value + 1)
        }
        channel3.close()
    }
}

fun function4(channel3: ReceiveChannel<Int>) {
    GlobalScope.launch {
        for (value in channel3) {
            println("Received: $value")
        }
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()

    function1(channel1)
    function2(channel1, channel2)
    function3(channel2, channel3)
    function4(channel3)

    delay(1000)   // Ensure main thread waits for coroutines to complete
}