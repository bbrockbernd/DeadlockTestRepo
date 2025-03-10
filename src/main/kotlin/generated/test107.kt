/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":6,"nChannels":1,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 1 different channels
- 6 different coroutines
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
package org.example.generated.test107
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA(private val channel: Channel<Int>) {
    suspend fun sendRepeatedly() {
        repeat(2) {
            delay(100L)
            channel.send(it)
        }
    }
}

class ClassB(private val channel: Channel<Int>) {
    suspend fun receiveAndSendBack() {
        for (item in channel) {
            channel.send(item * 2)
        }
    }
}

class ClassC(private val channel: Channel<Int>) {
    suspend fun consumeForever() {
        while (true) {
            val item = channel.receive()
            println("Consumed: $item")
        }
    }
}

class ClassD(private val channel: Channel<Int>) {
    suspend fun echoBack() {
        while (true) {
            val item = channel.receive()
            channel.send(item)
        }
    }
}

fun function1(channel: Channel<Int>) = runBlocking {
    val classA = ClassA(channel)
    launch { classA.sendRepeatedly() }
    repeat(2) { launch { classA.sendRepeatedly() } } // Duplicated to initiate multiple sends
}

fun function2(channel: Channel<Int>) = runBlocking {
    val classB = ClassB(channel)
    repeat(3) { launch { classB.receiveAndSendBack() } } // Multiple receivers to simulate deadlock
}

fun function3(channel: Channel<Int>) = runBlocking {
    val classC = ClassC(channel)
    launch { classC.consumeForever() }
}

fun function4(channel: Channel<Int>) = runBlocking {
    val classD = ClassD(channel)
    repeat(3) { launch { classD.echoBack() } } // Multiple echoes to clog the channel
}

fun main(): Unit{
    val channel = Channel<Int>()
    function1(channel)
    function2(channel)
    function3(channel)
    function4(channel)
}