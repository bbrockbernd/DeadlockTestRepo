/* 
{"deadlock":true,"nFunctions":7,"nCoroutines":2,"nChannels":7,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 7 different channels
- 2 different coroutines
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
package org.example.altered.test153
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()
    
    suspend fun funcA() {
        channelA.send(1)
        println("funcA sent 1 to channelA")
    }
    
    suspend fun funcB() {
        val value = channelB.receive()
        println("funcB received $value from channelB")
    }
}

class ClassB {
    val channelC = Channel<Int>()
    val channelD = Channel<Int>()
    
    suspend fun funcC() {
        channelC.send(2)
        println("funcC sent 2 to channelC")
    }
    
    suspend fun funcD() {
        val value = channelD.receive()
        println("funcD received $value from channelD")
    }
}

fun mainFunc(scope: CoroutineScope) {
    val channelE = Channel<Int>()
    val channelF = Channel<Int>()
    val channelG = Channel<Int>()
    
    scope.launch {
        val a = ClassA()
        val b = ClassB()
        launch {
            a.funcA()
            b.funcC()
        }
        launch {
            a.funcB()
            b.funcD()
        }
        funcE(channelE)
        funcF(channelF)
        funcG(channelG)
    }
}

suspend fun funcE(channel: Channel<Int>) {
    channel.send(3)
    println("funcE sent 3 to channel")
}

suspend fun funcF(channel: Channel<Int>) {
    val value = channel.receive()
    println("funcF received $value from channel")
}

suspend fun funcG(channel: Channel<Int>) {
    val value = channel.receive()
    println("funcG received $value from channel")
}

fun main(): Unit= runBlocking {
    mainFunc(this)
}

class RunChecker153: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}