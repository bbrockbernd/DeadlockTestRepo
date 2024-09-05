/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":2,"nChannels":7,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 7 different channels
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
package org.example.generated.test253
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ChannelTest {
    private val channel1 = Channel<Int>()
    private val channel2 = Channel<Int>()
    private val channel3 = Channel<Int>()
    private val channel4 = Channel<Int>()
    private val channel5 = Channel<Int>()
    private val channel6 = Channel<Int>()
    private val channel7 = Channel<Int>()
    
    fun function1() = runBlocking {
        launch {
            channel1.send(1)
            channel2.receive()
            channel3.send(3)
        }
        launch {
            channel2.send(2)
            channel1.receive() // deadlock
            channel4.send(4)
        }
    }
    
    suspend fun function2() = coroutineScope {
        launch {
            channel3.receive()
            channel5.send(5)
        }
        launch {
            channel6.send(6)
            channel3.receive() // deadlock
        }
    }
    
    fun function3() = runBlocking {
        launch {
            channel5.receive()
            channel4.send(7)
        }
    }
    
    suspend fun function4() = coroutineScope {
        launch {
            channel4.receive()
            channel7.send(8)
        }
        launch {
            channel7.receive()
            channel6.receive() // deadlock
        }
    }
}

fun main(): Unit{
    val test = ChannelTest()
    test.function1()
    runBlocking {
        test.function2()
        test.function3()
        test.function4()
    }
}