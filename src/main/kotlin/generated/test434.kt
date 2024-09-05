/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":8,"nChannels":7,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 7 different channels
- 8 different coroutines
- 1 different classes

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
package org.example.generated.test434
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ChannelTester {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()
    val channel6 = Channel<Int>()
    val channel7 = Channel<Int>()

    suspend fun sendToChannel1() {
        channel1.send(1)
        val received = channel2.receive()
        println("sendToChannel1 received from channel2: $received")
    }

    suspend fun sendToChannel2() {
        val received = channel1.receive()
        println("sendToChannel2 received from channel1: $received")
        channel2.send(2)
    }

    suspend fun sendToChannel3() {
        channel3.send(3)
        val received = channel4.receive()
        println("sendToChannel3 received from channel4: $received")
    }

    suspend fun sendToChannel4() {
        val received = channel3.receive()
        println("sendToChannel4 received from channel3: $received")
        channel4.send(4)
    }
    
    suspend fun sendToChannel5() {
        channel5.send(5)
        val received = channel6.receive()
        println("sendToChannel5 received from channel6: $received")
    }

    suspend fun sendToChannel6() {
        val received = channel5.receive()
        println("sendToChannel6 received from channel5: $received")
        channel6.send(6)
    }
    
    suspend fun sendToChannel7() {
        val received1 = channel1.receive()
        val received2 = channel2.receive()
        val received3 = channel3.receive()
        val received4 = channel4.receive()
        val received5 = channel5.receive()
        val received6 = channel6.receive()
        channel7.send(received1 + received2 + received3 + received4 + received5 + received6)
    }

    suspend fun receiveFromChannel7() {
        val result = channel7.receive()
        println("Result from channel7: $result")
    }
}

fun main(): Unit= runBlocking {
    val tester = ChannelTester()
    launch { tester.sendToChannel1() }
    launch { tester.sendToChannel2() }
    launch { tester.sendToChannel3() }
    launch { tester.sendToChannel4() }
    launch { tester.sendToChannel5() }
    launch { tester.sendToChannel6() }
    launch { tester.sendToChannel7() }
    launch { tester.receiveFromChannel7() }
}