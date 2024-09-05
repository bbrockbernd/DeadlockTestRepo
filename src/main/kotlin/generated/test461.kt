/* 
{"deadlock":true,"nFunctions":7,"nCoroutines":6,"nChannels":4,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 4 different channels
- 6 different coroutines
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
package org.example.generated.test461
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA(val channel1: Channel<Int>, val channel2: Channel<Int>) {
    suspend fun func1() {
        for (i in 1..5) {
            channel1.send(i)
        }
    }

    suspend fun func2() {
        for (i in 1..5) {
            channel2.send(i)
        }
    }

    suspend fun func3() {
        for (i in 1..5) {
            val received = channel1.receive()
            channel2.send(received)
        }
    }
}

class ClassB(val channel3: Channel<Int>, val channel4: Channel<Int>) {
    suspend fun func4() {
        for (i in 1..5) {
            channel3.send(i)
        }
    }

    suspend fun func5() {
        for (i in 1..5) {
            val received = channel3.receive()
            channel4.send(received)
        }
    }

    suspend fun func6() {
        for (i in 1..5) {
            val received = channel4.receive()
            channel3.send(received)
        }
    }
}

suspend fun func7(channel2: Channel<Int>, channel4: Channel<Int>) {
    for (i in 1..5) {
        val received = channel2.receive()
        channel4.send(received)
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()

    val classA = ClassA(channel1, channel2)
    val classB = ClassB(channel3, channel4)

    launch { classA.func1() }
    launch { classA.func2() }
    launch { classA.func3() }
    launch { classB.func4() }
    launch { classB.func5() }
    launch { classB.func6() }
    launch { func7(channel2, channel4) }
}