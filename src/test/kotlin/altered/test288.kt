/* 
{"deadlock":false,"nFunctions":8,"nCoroutines":7,"nChannels":4,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
- 4 different channels
- 7 different coroutines
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
package org.example.altered.test288
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Processor {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>(5)
    val channelC = Channel<String>()
    val channelD = Channel<String>(3)

    fun startProcessor() = runBlocking {
        coroutineScope {
            launch { func1() }
            launch { func2() }
            launch { func3() }
            launch { func4() }
            launch { func5() }
            launch { func6() }
            launch { func7() }
        }
    }

    suspend fun func1() {
        channelA.send(1)
        val received = channelB.receive()
        func8(received)
    }
    
    suspend fun func2() {
        val sentValue = 2
        channelB.send(sentValue)
        val received = channelA.receive()
        func8(received)
    }

    suspend fun func3() {
        channelC.send("Hello")
        channelD.send("World")
    }
    
    suspend fun func4() {
        val first = channelD.receive()
        val second = channelC.receive()
        println(first + second)
    }
    
    suspend fun func5() {
        channelC.send("Kotlin")
        val received = channelB.receive()
        func8(received)
    }
    
    suspend fun func6() {
        val result = channelD.receive()
        println("Received from channelD: $result")
    }
    
    suspend fun func7() {
        channelD.send("Coroutines")
        val received = channelA.receive()
        func8(received)
    }
    
    suspend fun func8(value: Int) {
        println("Processed value: $value")
    }
}

fun main(): Unit{
    val processor = Processor()
    processor.startProcessor()
}

class RunChecker288: RunCheckerBase() {
    override fun block() = main()
}