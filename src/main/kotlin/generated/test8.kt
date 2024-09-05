/* 
{"deadlock":true,"nFunctions":6,"nCoroutines":2,"nChannels":2,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 2 different channels
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
package org.example.generated.test8
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Processor {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()

    fun generateDataA() = runBlocking {
        launch {
            // coroutine 1
            repeat(5) {
                delay(100L)
                channelA.send(it)
                println("Sent to channelA: $it")
            }
        }
    }
    
    fun generateDataB() = runBlocking {
        launch {
            // coroutine 2
            repeat(5) {
                delay(200L)
                channelB.send(it)
                println("Sent to channelB: $it")
            }
        }
    }
    
    suspend fun processA() {
        val data = channelA.receive()
        println("Received from channelA: $data")
        processB(channelB) // introduces dependency on channelB
    }
    
    suspend fun processB(channel: Channel<Int>) {
        val data = channel.receive()
        println("Received from channelB: $data")
        processA() // introduces dependency on channelA
    }
    
    suspend fun handleDataA() {
        coroutineScope {
            repeat(5) {
                launch { processA() }
            }
        }
    }
    
    suspend fun handleDataB() {
        coroutineScope {
            repeat(5) {
                launch { processB(channelB) }
            }
        }
    }

    fun startProcessing() = runBlocking {
        launch { handleDataA() }
        launch { handleDataB() }
    }
}

fun main(): Unit{
    val processor = Processor()
    processor.generateDataA()
    processor.generateDataB()
    processor.startProcessing()
}