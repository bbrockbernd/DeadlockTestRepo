/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":7,"nChannels":7,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 7 different channels
- 7 different coroutines
- 5 different classes

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
package org.example.generated.test2
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA(val channel: Channel<Int>)
class ClassB(val channel: Channel<Int>)
class ClassC(val channel1: Channel<Int>, val channel2: Channel<Int>)
class ClassD(val channel1: Channel<Int>, val channel2: Channel<Int>)
class ClassE(val channel1: Channel<Int>, val channel2: Channel<Int>)

fun func1(channel1: Channel<Int>, channel2: Channel<Int>) {
    runBlocking {
        launch {
            channel1.send(1)
            val received = channel2.receive()
            println("func1 received: $received")
        }
    }
}

fun func2(channel: Channel<Int>) {
    runBlocking {
        launch {
            val received = channel.receive()
            println("func2 received: $received")
            channel.send(received + 1)
        }
    }
}

fun func3(channel: Channel<Int>) {
    runBlocking {
        launch {
            channel.send(3)
            val received = channel.receive()
            println("func3 received: $received")
        }
    }
}

fun func4(channel: Channel<Int>) {
    runBlocking {
        launch {
            val received = channel.receive()
            println("func4 received: $received")
            channel.send(received + 2)
        }
    }
}

fun main(): Unit{
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()
    val channel6 = Channel<Int>()
    val channel7 = Channel<Int>()

    val a = ClassA(channel1)
    val b = ClassB(channel2)
    val c = ClassC(channel3, channel4)
    val d = ClassD(channel5, channel6)
    val e = ClassE(channel3, channel7)

    runBlocking {
        launch { func1(a.channel, b.channel) }
        launch { func2(b.channel) }
        launch { func3(c.channel1) }
        launch { func4(d.channel1) }
        launch { func1(e.channel1, d.channel2) }
        launch { func4(e.channel2) }
        launch { func3(c.channel2) }
    }
}