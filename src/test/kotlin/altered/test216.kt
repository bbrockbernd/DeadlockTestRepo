/* 
{"deadlock":true,"nFunctions":2,"nCoroutines":2,"nChannels":4,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 4 different channels
- 2 different coroutines
- 1 different classes

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
package org.example.altered.test216
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class DeadlockExample {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()
    val channelC = Channel<Int>()
    val channelD = Channel<Int>()

    fun firstFunction() = runBlocking {
        launch { 
            val valueA = channelA.receive()
            channelB.send(valueA)
        }
        launch { 
            val valueB = channelB.receive()
            channelC.send(valueB)
        }
        launch {
            val valueTx = channelD.receive()
            channelA.send(valueTx)
        }
    }

    fun secondFunction() = runBlocking {
        launch { 
            val valueC = channelC.receive()
            channelD.send(valueC)
        }
        launch {
            channelD.send(42)
            val valueTx = channelA.receive()
            channelB.send(valueTx)
        }
    }
}

fun main(): Unit= runBlocking {
    val example = DeadlockExample()

    launch { example.firstFunction() }
    launch { example.secondFunction() }
}

class RunChecker216: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}