/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":8,"nChannels":2,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 2 different channels
- 8 different coroutines
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
package org.example.altered.test106
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA(private val channel1: Channel<Int>, private val channel2: Channel<String>) {
    suspend fun functionA() {
        for (i in 1..5) {
            channel1.send(i)
        }
    }
}

class ClassB(private val channel1: Channel<Int>, private val channel2: Channel<String>) {
    suspend fun functionB() {
        for (i in 1..5) {
            val value = channel1.receive()
            channel2.send("Message $value")
        }
    }
}

class ClassC(private val channel1: Channel<Int>, private val channel2: Channel<String>) {
    suspend fun functionC() {
        for (i in 1..5) {
            println(channel2.receive())
        }
    }
}

class ClassD(private val channel1: Channel<Int>) {
    suspend fun functionA() {
        for (i in 6..10) {
            channel1.send(i)
        }
    }
}

class ClassE(private val channel2: Channel<String>) {
    suspend fun functionB() {
        for (i in 6..10) {
            channel2.send("Message $i")
        }
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<String>()

    val classA = ClassA(channel1, channel2)
    val classB = ClassB(channel1, channel2)
    val classC = ClassC(channel1, channel2)
    val classD = ClassD(channel1)
    val classE = ClassE(channel2)

    launch { classA.functionA() }
    launch { classB.functionB() }
    launch { classC.functionC() }

    launch { classD.functionA() }
    launch { classB.functionB() }
    launch { classE.functionB() }
    launch { classB.functionB() }
    launch { classC.functionC() }
}

class RunChecker106: RunCheckerBase() {
    override fun block() = main()
}