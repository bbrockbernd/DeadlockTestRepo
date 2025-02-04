/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":2,"nChannels":5,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 5 different channels
- 2 different coroutines
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
package org.example.altered.test45
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Sender(val ch1: Channel<Int>, val ch2: Channel<Int>) {
    suspend fun sendValues(value: Int) {
        ch1.send(value)
        ch2.send(value)
    }
}

class Receiver(val ch3: Channel<Int>, val ch4: Channel<Int>, val ch5: Channel<Int>) {
    suspend fun receiveValues(): Int {
        val value1 = ch3.receive()
        val value2 = ch4.receive()
        val value3 = ch5.receive()
        return value1 + value2 + value3
    }
}

suspend fun intermediateProcess(inputCh: Channel<Int>, outputCh1: Channel<Int>, outputCh2: Channel<Int>) {
    val value = inputCh.receive()
    outputCh1.send(value)
    outputCh2.send(value)
}

fun main(): Unit= runBlocking {
    val ch1 = Channel<Int>()
    val ch2 = Channel<Int>()
    val ch3 = Channel<Int>()
    val ch4 = Channel<Int>()
    val ch5 = Channel<Int>()
    
    val sender = Sender(ch1, ch2)
    val receiver = Receiver(ch3, ch4, ch5)

    launch {
        sender.sendValues(42)
    }

    launch {
        intermediateProcess(ch1, ch3, ch5)
    }

    val finalResult = receiver.receiveValues()
    println(finalResult)
}

class RunChecker45: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}