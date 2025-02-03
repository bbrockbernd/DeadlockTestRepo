/* 
{"deadlock":true,"nFunctions":7,"nCoroutines":2,"nChannels":3,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 3 different channels
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
package org.example.altered.test397
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ChannelManager {
    private val channel1 = Channel<Int>()
    private val channel2 = Channel<Int>()
    private val channel3 = Channel<Int>()

    suspend fun sendToChannel1(value: Int) {
        channel1.send(value)
    }

    suspend fun receiveFromChannel1(): Int {
        return channel1.receive()
    }

    suspend fun sendToChannel2(value: Int) {
        channel2.send(value)
    }

    suspend fun receiveFromChannel2(): Int {
        return channel2.receive()
    }

    suspend fun sendToChannel3(value: Int) {
        channel3.send(value)
    }

    suspend fun receiveFromChannel3(): Int {
        return channel3.receive()
    }

    fun initiateDeadlock() = runBlocking {
        val job1 = launch {
            coroutineScope {
                sendToChannel1(1)
                channel2.send(receiveFromChannel1())
            }
        }

        val job2 = launch {
            coroutineScope {
                sendToChannel2(2)
                channel1.send(receiveFromChannel2())
            }
        }

        job1.join()
        job2.join()
    }
}

fun main(): Unit{
    val manager = ChannelManager()
    manager.initiateDeadlock()
}

class RunChecker397: RunCheckerBase() {
    override fun block() = main()
}