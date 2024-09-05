/* 
{"deadlock":true,"nFunctions":6,"nCoroutines":5,"nChannels":7,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 7 different channels
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
package org.example.generated.test324
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class A(val channel1: Channel<Int>, val channel2: Channel<Int>) {
    suspend fun sendDataToChannel1(value: Int) {
        channel1.send(value)
    }

    suspend fun receiveDataFromChannel2(): Int {
        return channel2.receive()
    }
}

class B(val channel3: Channel<Int>, val channel4: Channel<Int>) {
    suspend fun sendDataToChannel3(value: Int) {
        channel3.send(value)
    }

    suspend fun receiveDataFromChannel4(): Int {
        return channel4.receive()
    }
}

class C(val channel5: Channel<Int>, val channel6: Channel<Int>) {
    suspend fun sendDataToChannel5(value: Int) {
        channel5.send(value)
    }

    suspend fun receiveDataFromChannel6(): Int {
        return channel6.receive()
    }
}

fun function1(channel7: Channel<Int>, a: A) {
    runBlocking {
        launch {
            val received = a.receiveDataFromChannel2()
            channel7.send(received)
        }
    }
}

fun function2(channel1: Channel<Int>, b: B) {
    runBlocking {
        launch {
            val received = channel1.receive()
            b.sendDataToChannel3(received)
        }
    }
}

fun function3(channel3: Channel<Int>, c: C) {
    runBlocking {
        launch {
            val received = b.receiveDataFromChannel4()
            c.sendDataToChannel5(received)
        }
    }
}

fun function4(channel5: Channel<Int>, a: A) {
    runBlocking {
        launch {
            val received = c.receiveDataFromChannel6()
            a.sendDataToChannel1(received)
        }
    }
}

fun function5(channel2: Channel<Int>, b: B) {
    runBlocking {
        launch {
            b.sendDataToChannel4(50)
            channel2.send(60)
        }
    }
}

fun function6(channel6: Channel<Int>, a: A, b: B, c: C) {
    runBlocking {
        launch {
            channel6.send(a.receiveDataFromChannel2() + b.receiveDataFromChannel4() + c.receiveDataFromChannel6())
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
    
    val a = A(channel1, channel2)
    val b = B(channel3, channel4)
    val c = C(channel5, channel6)

    runBlocking {
        launch {
            function1(channel7, a)
            function2(channel1, b)
            function3(channel3, c)
            function4(channel5, a)
            function5(channel2, b)
            function6(channel6, a, b, c)
        }
    }
}