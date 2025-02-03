/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":5,"nChannels":4,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 4 different channels
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
package org.example.altered.test573
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ChannelManager {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    
    suspend fun produceNumbers() {
        for (i in 1..5) {
            channel1.send(i)
            channel2.send(i * 2)
        }
        channel1.close()
        channel2.close()
    }
    
    suspend fun processNumbers() {
        for (i in channel3) {
            channel4.send(i * 3)
        }
        channel4.close()
    }
}

fun main(): Unit= runBlocking {
    val manager = ChannelManager()
    
    launch {
        manager.produceNumbers()
    }
    
    launch {
        for (i in manager.channel1) {
            manager.channel3.send(i + 1)
        }
        manager.channel3.close()
    }

    launch {
        for (i in manager.channel2) {
            manager.channel3.send(i + 2)
        }
        manager.channel3.close()
    }

    launch {
        manager.processNumbers()
    }

    launch {
        for (i in manager.channel4) {
            println("Received: $i")
        }
    }
}

class RunChecker573: RunCheckerBase() {
    override fun block() = main()
}