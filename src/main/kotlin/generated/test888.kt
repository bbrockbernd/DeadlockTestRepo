/* 
{"deadlock":true,"nFunctions":2,"nCoroutines":5,"nChannels":5,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 5 different channels
- 5 different coroutines
- 2 different classes

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
package org.example.generated.test888
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class MyClass1(val channelA: Channel<Int>, val channelB: Channel<Int>) {
    suspend fun sendData() {
        coroutineScope {
            launch { 
                channelA.send(1)
                channelB.receive()
            }
        }
    }
}

class MyClass2(val channelC: Channel<Int>, val channelD: Channel<Int>, val channelE: Channel<Int>) {
    suspend fun processData() {
        coroutineScope {
            launch { 
                val value = channelC.receive()
                channelD.send(value)
                channelE.receive()
            }
        }
    }
}

fun main(): Unit= runBlocking {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()
    val channelC = Channel<Int>()
    val channelD = Channel<Int>()
    val channelE = Channel<Int>()

    val myClass1 = MyClass1(channelA, channelB)
    val myClass2 = MyClass2(channelC, channelD, channelE)

    launch {
        myClass1.sendData() // Coroutine 1
    }
    
    launch {
        myClass2.processData() // Coroutine 2
    }
    
    launch {
        channelB.send(2)
        channelC.send(3)
    }
    
    launch {
        channelA.receive()
        channelD.receive()
    }
    
    launch {
        channelE.send(4)
    }
}