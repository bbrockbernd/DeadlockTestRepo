/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":5,"nChannels":5,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 5 different channels
- 5 different coroutines
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
package org.example.generated.test673
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

// Class A definition
class ClassA(private val channelA: Channel<Int>, private val channelB: Channel<Int>) {
    suspend fun sendToChannelA(value: Int) {
        channelA.send(value)
    }

    suspend fun receiveFromChannelB(): Int {
        return channelB.receive()
    }
}

// Class B definition
class ClassB(private val channelC: Channel<Int>, private val channelD: Channel<Int>) {
    suspend fun sendToChannelC(value: Int) {
        channelC.send(value)
    }

    suspend fun receiveFromChannelD(): Int {
        return channelD.receive()
    }
}

fun main(): Unit= runBlocking {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()
    val channelC = Channel<Int>()
    val channelD = Channel<Int>()
    val channelE = Channel<Int>()

    val classA = ClassA(channelA, channelB)
    val classB = ClassB(channelC, channelD)

    launch {
        println("Function 1: Received ${classA.receiveFromChannelB()}")
    }

    launch {
        classA.sendToChannelA(1)
        println("Function 2: Sent to Channel A")
    }

    launch {
        val value = channelA.receive()
        println("Function 3: Sent to Channel B")
        channelB.send(value)
    }

    launch {
        classB.sendToChannelC(2)
        println("Function 4: Sent to Channel C")
    }

    launch {
        val value = classB.receiveFromChannelD()
        println("Function 5: Received from Channel D and sent to Channel E")
        channelE.send(value)
    }

    // Initiate the process
    classB.sendToChannelD(3)

    // Receive final value for completion
    println("Main received from Channel E: ${channelE.receive()}")
}