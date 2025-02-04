/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":4,"nChannels":4,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 4 different channels
- 4 different coroutines
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
package org.example.altered.test907
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class ExampleClass {
    val channel1 = Channel<Int>()
    val channel2 = Channel<String>()
    val channel3 = Channel<Double>()
    val channel4 = Channel<Long>()
    
    suspend fun producer1() {
        channel1.send(1)
        delay(50)
        channel1.send(2)
    }

    suspend fun producer2() {
        channel2.send("Hello")
        delay(50)
        channel2.send("World")
    }
    
    suspend fun producer3() {
        channel3.send(3.14)
        delay(50)
        channel3.send(1.618)
    }

    suspend fun consumer1() {
        println(channel1.receive())
        println(channel2.receive())
    }

    suspend fun consumer2() {
        println(channel3.receive())
        println(channel4.receive())
    }
}

fun main(): Unit= runBlocking {
    val example = ExampleClass()

    launch {
        example.producer1()
    }
    
    launch {
        example.producer2()
    }
    
    launch {
        example.consumer1()
    }
    
    launch {
        example.consumer2()
    }

    example.channel4.send(123456789L)
}

class RunChecker907: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}