/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":6,"nChannels":5,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 5 different channels
- 6 different coroutines
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
package org.example.generated.test389
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class DeadlockSample {
    val channelA = Channel<Int>(1)
    val channelB = Channel<Int>(1)
    val channelC = Channel<Int>(1)
    val channelD = Channel<Int>(1)
    val channelE = Channel<Int>(1)

    fun func1() {
        runBlocking {
            launch {
                val x = channelA.receive()
                channelB.send(x + 1)
            }
        }
    }

    fun func2() {
        runBlocking {
            launch {
                val y = channelB.receive()
                channelC.send(y + 1)
            }
        }
    }

    fun func3() {
        runBlocking {
            launch {
                val z = channelC.receive()
                channelD.send(z + 1)
            }
        }
    }

    fun func4() {
        runBlocking {
            launch {
                val w = channelD.receive()
                channelE.send(w + 1)
            }
        }
    }

    fun func5() {
        runBlocking {
            launch {
                val v = channelE.receive()
                channelA.send(v + 1)  // This creates a circular dependency causing the deadlock
            }
        }
    }

    fun startCoroutines() {
        runBlocking {
            launch { func1() }
            launch { func2() }
            launch { func3() }
            launch { func4() }
            launch { func5() }
            launch {
                channelA.send(1)  // Start the chain reaction
            }
        }
    }
}

fun main(): Unit{
    val sample = DeadlockSample()
    sample.startCoroutines()
}