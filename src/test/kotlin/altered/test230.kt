/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":7,"nChannels":2,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 2 different channels
- 7 different coroutines
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
package org.example.altered.test230
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA(val channel1: Channel<Int>, val channel2: Channel<Int>) {
    suspend fun function1() {
        repeat(5) {
            println("ClassA.function1 sending $it")
            channel1.send(it)
        }
        println("ClassA.function1 completed")
    }

    suspend fun function2() {
        while (true) {
            println("ClassA.function2 receiving")
            val received = channel2.receive()
            println("ClassA.function2 received $received")
        }
    }
}

class ClassB(val channel1: Channel<Int>, val channel2: Channel<Int>) {
    suspend fun function3() {
        while (true) {
            println("ClassB.function3 receiving")
            val received = channel1.receive()
            println("ClassB.function3 received $received")
        }
    }

    suspend fun function4() {
        repeat(5) {
            println("ClassB.function4 sending $it")
            channel2.send(it)
        }
        println("ClassB.function4 completed")
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    
    val classA = ClassA(channel1, channel2)
    val classB = ClassB(channel1, channel2)

    launch { classA.function1() }
    launch { classA.function2() }
    launch { classB.function3() }
    launch { classB.function4() }
    launch { classB.function4() }
    launch { classA.function1() }
    launch { classA.function2() }
}

class RunChecker230: RunCheckerBase() {
    override fun block() = main()
}