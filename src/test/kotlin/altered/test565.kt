/* 
{"deadlock":true,"nFunctions":2,"nCoroutines":2,"nChannels":1,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 1 different channels
- 2 different coroutines
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
package org.example.altered.test565
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA(val channel: Channel<Int>) {
    suspend fun send(value: Int) {
        channel.send(value)
    }
}

class ClassB(val channel: Channel<Int>) {
    suspend fun receive(): Int {
        return channel.receive()
    }
}

class DeadlockDetector {
    fun detect() = runBlocking {
        val channel = Channel<Int>()

        val classA = ClassA(channel)
        val classB = ClassB(channel)

        launch {
            classA.send(1)
        }

        launch {
            classB.receive()
        }
    }
}

fun main(): Unit{
    val detector = DeadlockDetector()
    detector.detect()
}

class RunChecker565: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}