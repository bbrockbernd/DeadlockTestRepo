/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":4,"nChannels":1,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 1 different channels
- 4 different coroutines
- 0 different classes

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
package org.example.generated.test737
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

val channel = Channel<Int>()

fun main(): Unit= runBlocking {
    launch { coroutineA() }
    launch { coroutineB() }
    launch { coroutineC() }
    launch { coroutineD() }
}

suspend fun coroutineA() {
    channel.send(1)
    function1()
}

suspend fun coroutineB() {
    function2()
    channel.send(2)
}

suspend fun coroutineC() {
    val value = channel.receive()
    println("coroutineC received: $value")
    function3()
}

suspend fun coroutineD() {
    val value = channel.receive()
    println("coroutineD received: $value")
    function4()
}

fun function1() {
    runBlocking { 
        println("function1 started")
    }
}

fun function2() {
    runBlocking { 
        println("function2 started")
    }
}

fun function3() {
    runBlocking { 
        println("function3 started")
    }
}

fun function4() {
    runBlocking { 
        println("function4 started")
    }
}

fun function5() {
    runBlocking { 
        println("function5 started")
    }
}