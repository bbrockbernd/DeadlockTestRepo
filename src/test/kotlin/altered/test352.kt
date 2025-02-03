/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":6,"nChannels":6,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 6 different channels
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
package org.example.altered.test352
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Processor {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()
    val channelC = Channel<Int>()
    val channelD = Channel<Int>()
    val channelE = Channel<Int>()
    val channelF = Channel<Int>()

    suspend fun processChannels() {
        coroutineScope {
            launch { 
                for (i in 0 until 10) {
                    channelA.send(i)
                    channelB.send(i * 2)
                }
            }
            launch {
                for (i in 0 until 10) {
                    val a = channelA.receive()
                    val b = channelB.receive()
                    channelC.send(a + b)
                }
            }
            launch {
                for (i in 0 until 10) {
                    val c = channelC.receive()
                    channelD.send(c * 2)
                }
            }
            launch {
                for (i in 0 until 10) {
                    val d = channelD.receive()
                    channelE.send(d - 1)
                }
            }
            launch {
                for (i in 0 until 10) {
                    val e = channelE.receive()
                    channelF.send(e + 5)
                }
            }
            launch {
                for (i in 0 until 10) {
                    println(channelF.receive())
                }
            }
        }
    }
}

fun main(): Unit= runBlocking {
    val processor = Processor()
    processor.processChannels()
}

class RunChecker352: RunCheckerBase() {
    override fun block() = main()
}