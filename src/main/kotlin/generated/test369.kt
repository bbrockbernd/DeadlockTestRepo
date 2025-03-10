/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":6,"nChannels":2,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 2 different channels
- 6 different coroutines
- 4 different classes

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
package org.example.generated.test369
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA(val channel: Channel<Int>)
class ClassB(val channel: Channel<Int>)
class ClassC
class ClassD(val resultChannel: Channel<Int>)

fun producerA(channelA: Channel<Int>) {
    GlobalScope.launch {
        for (i in 1..10) {
            channelA.send(i)
        }
    }
}

fun producerB(channelB: Channel<Int>) {
    GlobalScope.launch {
        for (i in 11..20) {
            channelB.send(i)
        }
    }
}

fun mainFunction(cA: ClassA, cB: ClassB, cC: ClassC, cD: ClassD) = runBlocking {
    launch {
        for (i in 1..10) {
            val receivedA = cA.channel.receive()
            cD.resultChannel.send(receivedA)
        }
    }
    
    launch {
        for (i in 1..10) {
            val receivedB = cB.channel.receive()
            cD.resultChannel.send(receivedB)
        }
    }
    
    launch {
        for (i in 1..20) {
            val result = cD.resultChannel.receive()
            println("Result: $result")
        }
    }
}

fun main(): Unit= runBlocking {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()
    val resultChannel = Channel<Int>()
    
    val cA = ClassA(channelA)
    val cB = ClassB(channelB)
    val cC = ClassC()
    val cD = ClassD(resultChannel)
    
    producerA(channelA)
    producerB(channelB)
    
    mainFunction(cA, cB, cC, cD)
}