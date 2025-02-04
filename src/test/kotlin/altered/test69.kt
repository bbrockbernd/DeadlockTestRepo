/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":6,"nChannels":2,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 2 different channels
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
package org.example.altered.test69
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA(val channel1: Channel<Int>, val channel2: Channel<String>) {
    suspend fun functionA() {
        repeat(5) {
            channel1.send(it)
        }
    }
    
    suspend fun functionB() {
        for (i in 0 until 5) {
            val value = channel1.receive()
            channel2.send("Received $value")
        }
    }
}

class ClassB(val channel2: Channel<String>) {
    suspend fun functionC() {
        repeat(5) {
            println(channel2.receive())
        }
    }
}

fun main(): Unit = runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<String>()
    
    val classA = ClassA(channel1, channel2)
    val classB = ClassB(channel2)
    
    launch { classA.functionA() }
    launch { classA.functionB() }
    launch { classB.functionC() }
    
    coroutineScope {
        launch { classA.functionA() }
        launch { classA.functionB() }
        launch { classB.functionC() }
    }
}

class RunChecker69: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}