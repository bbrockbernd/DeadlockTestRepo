/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":3,"nChannels":1,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 1 different channels
- 3 different coroutines
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
package org.example.altered.test989
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA {
    suspend fun function1(channel: Channel<Int>) {
        repeat(5) {
            channel.send(it)
        }
        channel.close()
    }
}

class ClassB {
    suspend fun function2(channel: Channel<Int>) {
        for (msg in channel) {
            println("Received: $msg by ClassB")
        }
    }
}

class ClassC {
    suspend fun function3(channel: Channel<Int>) {
        for (msg in channel) {
            println("Received: $msg by ClassC")
        }
    }
}

fun main(): Unit= runBlocking<Unit> {
    val channel = Channel<Int>()
    val classA = ClassA()
    val classB = ClassB()
    val classC = ClassC()

    launch {
        classA.function1(channel)
    }
    launch {
        classB.function2(channel)
    }
    launch {
        classC.function3(channel)
    }
}

class RunChecker989: RunCheckerBase() {
    override fun block() = main()
}