/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":1,"nChannels":7,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 7 different channels
- 1 different coroutines
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
package org.example.generated.test488
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class DataA(val ch1: Channel<String>, val ch2: Channel<String>)

class DataB(val ch3: Channel<String>, val ch4: Channel<String>, val ch5: Channel<String>, val ch6: Channel<String>, val ch7: Channel<String>)

suspend fun firstFunction(dataA: DataA) {
    dataA.ch1.send("From ch1")
    val received = dataA.ch2.receive()
    println("First Function Received: $received")
}

suspend fun secondFunction(dataB: DataB) {
    val received1 = dataB.ch3.receive()
    println("Second Function Received1: $received1")
    dataB.ch4.send("From ch4")
    dataB.ch5.send("From ch5")
    val received2 = dataB.ch6.receive()
    println("Second Function Received2: $received2")
}

suspend fun thirdFunction(dataA: DataA, dataB: DataB) {
    dataA.ch2.send("From ch2")
    val received1 = dataB.ch7.receive()
    println("Third Function Received1: $received1")
    dataB.ch3.send("From ch3")
    dataA.ch1.send("From ch1")
}

fun main(): Unit= runBlocking {
    val ch1 = Channel<String>()
    val ch2 = Channel<String>()
    val ch3 = Channel<String>()
    val ch4 = Channel<String>()
    val ch5 = Channel<String>()
    val ch6 = Channel<String>()
    val ch7 = Channel<String>()

    val dataA = DataA(ch1, ch2)
    val dataB = DataB(ch3, ch4, ch5, ch6, ch7)

    launch {
        firstFunction(dataA)
    }

    launch {
        secondFunction(dataB)
    }

    launch {
        thirdFunction(dataA, dataB)
    }
}