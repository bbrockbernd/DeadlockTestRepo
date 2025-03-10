/* 
{"deadlock":true,"nFunctions":6,"nCoroutines":5,"nChannels":6,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 6 different channels
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
package org.example.generated.test221
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA(val chA1: Channel<Int>, val chA2: Channel<Int>) {
    suspend fun sendToChannelA1(value: Int) {
        chA1.send(value)
    }

    suspend fun receiveFromChannelA2() {
        chA2.receive()
    }
}

class ClassB(val chB1: Channel<Int>, val chB2: Channel<Int>) {
    suspend fun sendToChannelB1(value: Int) {
        chB1.send(value)
    }

    suspend fun receiveFromChannelB2() {
        chB2.receive()
    }
}

suspend fun function1(ch1: Channel<Int>) {
    ch1.send(1)
    ch1.receive()
}

suspend fun function2(ch2: Channel<Int>, ch3: Channel<Int>) {
    coroutineScope {
        launch {
            ch2.send(2)
        }
        launch {
            ch3.receive()
        }
    }
}

suspend fun function3(ch4: Channel<Int>, ch5: Channel<Int>) {
    coroutineScope {
        launch {
            ch4.send(3)
        }
        launch {
            ch5.receive()
        }
    }
}

fun function4(ch6: Channel<Int>) = runBlocking {
    ch6.send(4)
    ch6.receive()
}

fun main(): Unit= runBlocking {
    val chA1 = Channel<Int>()
    val chA2 = Channel<Int>()
    val classA = ClassA(chA1, chA2)

    val chB1 = Channel<Int>()
    val chB2 = Channel<Int>()
    val classB = ClassB(chB1, chB2)

    val chC1 = Channel<Int>()
    val chC2 = Channel<Int>()
    val chC3 = Channel<Int>()
    val chC4 = Channel<Int>()
    val chC5 = Channel<Int>()
    val chC6 = Channel<Int>()

    launch {
        function1(chC1)
    }

    launch {
        function2(chC2, chC3)
    }

    launch {
        function3(chC4, chC5)
    }

    launch {
        function4(chC6)
    }

    launch {
        classA.sendToChannelA1(5)
        classA.receiveFromChannelA2()
    }

    launch {
        classB.sendToChannelB1(6)
        classB.receiveFromChannelB2()
    }
}