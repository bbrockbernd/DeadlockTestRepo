/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":5,"nChannels":4,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 4 different channels
- 5 different coroutines
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
package org.example.generated.test246
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class A(val channel1: Channel<Int>, val channel2: Channel<Int>)
class B(val channel3: Channel<Int>)
class C(val channel4: Channel<Int>)

fun function1(channel1: Channel<Int>, value: Int) {
    runBlocking {
        channel1.send(value)
    }
}

fun function2(channel2: Channel<Int>): Int {
    var result = 0
    runBlocking {
        result = channel2.receive()
    }
    return result
}

fun function3(channel1: Channel<Int>, channel3: Channel<Int>) {
    runBlocking {
        val value = channel1.receive()
        channel3.send(value)
    }
}

fun function4(channel3: Channel<Int>, channel4: Channel<Int>) {
    runBlocking {
        val value = channel3.receive()
        channel4.send(value)
    }
}

fun function5(channel4: Channel<Int>, channel2: Channel<Int>) {
    runBlocking {
        val value = channel4.receive()
        channel2.send(value)
    }
}

fun main(): Unit{
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()

    val a = A(channel1, channel2)
    val b = B(channel3)
    val c = C(channel4)

    runBlocking {
        launch {
            function1(a.channel1, 1)
        }
        launch {
            function3(a.channel1, b.channel3)
        }
        launch {
            function4(b.channel3, c.channel4)
        }
        launch {
            function5(c.channel4, a.channel2)
        }
        launch {
            println("Received value: ${function2(a.channel2)}")
        }
    }
}