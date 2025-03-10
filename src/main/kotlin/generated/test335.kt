/* 
{"deadlock":true,"nFunctions":8,"nCoroutines":2,"nChannels":1,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
- 1 different channels
- 2 different coroutines
- 3 different classes

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
package org.example.generated.test335
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA(val channel: Channel<Int>) {
    suspend fun functionA() {
        channel.send(10)
        println("ClassA functionA completed.")
    }

    suspend fun functionB() {
        val value = channel.receive()
        println("ClassA functionB received: $value")
    }
}

class ClassB(val channel: Channel<Int>) {
    suspend fun functionC() {
        channel.send(20)
        println("ClassB functionC completed.")
    }

    suspend fun functionD() {
        val value = channel.receive()
        println("ClassB functionD received: $value")
    }
}

class ClassC(val channel: Channel<Int>) {
    suspend fun functionE() {
        channel.send(30)
        println("ClassC functionE completed.")
    }

    suspend fun functionF() {
        val value = channel.receive()
        println("ClassC functionF received: $value")
    }
}

suspend fun mainFunctionA(channel: Channel<Int>) {
    val classA = ClassA(channel)
    classA.functionA()
    classA.functionB()
}

suspend fun mainFunctionB(channel: Channel<Int>) {
    val classB = ClassB(channel)
    classB.functionC()
    classB.functionD()
}

fun main(): Unit= runBlocking {
    val channel = Channel<Int>()

    launch {
        mainFunctionA(channel)
    }

    launch {
        mainFunctionB(channel)
    }
    
    delay(1000L) // Just to prevent main from exiting immediately
}