/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":1,"nChannels":2,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 2 different channels
- 1 different coroutines
- 3 different classes

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
package org.example.altered.test609
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.launch

class A(val channel1: Channel<Int>, val channel2: Channel<Int>)

class B(val channel1: Channel<Int>)

class C(val channel2: Channel<Int>)

fun sendToChannel1(channel1: Channel<Int>) {
    channel1.send(1)
}

fun sendToChannel2(channel2: Channel<Int>) {
    channel2.send(2)
}

fun receiveFromChannel1(channel1: Channel<Int>): Int {
    return channel1.receive()
}

fun receiveFromChannel2(channel2: Channel<Int>): Int {
    return channel2.receive()
}

fun communicate(a: A, b: B, c: C) {
    runBlocking {
        launch {
            sendToChannel1(a.channel1)
            sendToChannel2(a.channel2)

            val received1 = receiveFromChannel1(a.channel1)
            val received2 = receiveFromChannel2(a.channel2)

            println("A received: $received1 and $received2")
        }

        launch {
            sendToChannel1(b.channel1)
            val received1 = receiveFromChannel1(b.channel1)
            println("B received: $received1")
        }

        launch {
            sendToChannel2(c.channel2)
            val received2 = receiveFromChannel2(c.channel2)
            println("C received: $received2")
        }
    }
}

fun main(): Unit{
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val a = A(channel1, channel2)
    val b = B(channel1)
    val c = C(channel2)

    communicate(a, b, c)
}

class RunChecker609: RunCheckerBase() {
    override fun block() = main()
}