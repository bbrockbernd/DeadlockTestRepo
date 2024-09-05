/* 
{"deadlock":true,"nFunctions":6,"nCoroutines":1,"nChannels":2,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 2 different channels
- 1 different coroutines
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
package org.example.generated.test406
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Processor {
    
    private val channel1 = Channel<Int>()
    private val channel2 = Channel<Int>()

    fun startProcesses() {
        runBlocking {
            launch { process1() }
            launch { process2() }
        }
    }

    private suspend fun process1() {
        channel1.send(1)
        val received = channel2.receive()
        println("Process1 received: $received")
    }

    private suspend fun process2() {
        channel2.send(2)
        val received = channel1.receive()
        println("Process2 received: $received")
    }

    fun callProcess3() = runBlocking { process3() }

    private suspend fun process3() {
        coroutineScope {
            launch { process4() }
            launch { process5() }
        }
    }

    private suspend fun process4() {
        channel1.send(3)
        val received = channel2.receive()
        println("Process4 received: $received")
    }

    private suspend fun process5() {
        channel2.send(4)
        val received = channel1.receive()
        println("Process5 received: $received")
    }
}

fun main(): Unit{
    val processor = Processor()
    processor.startProcesses()
    processor.callProcess3()
}