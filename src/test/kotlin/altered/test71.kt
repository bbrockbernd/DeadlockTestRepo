/* 
{"deadlock":true,"nFunctions":7,"nCoroutines":5,"nChannels":7,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 7 different channels
- 5 different coroutines
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
package org.example.altered.test71
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class DeadlockExample {
    private val channel1 = Channel<Int>()
    private val channel2 = Channel<Int>()
    private val channel3 = Channel<Int>()
    private val channel4 = Channel<Int>()
    private val channel5 = Channel<Int>()
    private val channel6 = Channel<Int>()
    private val channel7 = Channel<Int>()

    fun function1() {
        runBlocking {
            launch { function2() }
            launch { function3() }
            launch { function4() }
            launch { function5() }
            function6()
        }
    }

    fun function2() {
        runBlocking {
            launch {
                channel1.send(1)
                channel2.receive()
            }
        }
    }

    fun function3() {
        runBlocking {
            launch {
                channel2.send(2)
            }
            launch {
                channel3.receive()
                channel4.receive()
            }
        }
    }

    fun function4() {
        runBlocking {
            launch {
                channel3.send(3)
            }
            launch {
                channel5.receive()
                channel1.receive()
            }
        }
    }

    fun function5() {
        runBlocking {
            launch {
                channel4.send(4)
                channel5.send(5)
            }
        }
    }

    suspend fun function6() {
        coroutineScope {
            launch {
                channel6.receive()
                channel7.send(6)
            }
            launch {
                channel7.receive()
                channel6.send(7)
            }
        }
    }

    suspend fun function7() {
        coroutineScope {
            launch {
                channel5.send(8)
                channel3.send(9)
            }
        }
    }
}

fun main(): Unit{
    val deadlockExample = DeadlockExample()
    deadlockExample.function1()
}

class RunChecker71: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}