/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":2,"nChannels":5,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 5 different channels
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
package org.example.generated.test805
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ExampleClass(val c1: Channel<String>, val c2: Channel<String>, val c3: Channel<String>)

fun funcA(c4: Channel<Int>) = runBlocking {
    val data = c4.receive()
    println("Received in funcA: $data")
}

suspend fun funcB(example: ExampleClass) {
    val msg = example.c1.receive()
    println("Received in funcB: $msg")
    example.c2.send(msg)
}

fun funcC(c5: Channel<Double>) = runBlocking {
    val value = System.currentTimeMillis().toDouble()
    c5.send(value)
    println("Sent in funcC: $value")
}

fun main(): Unit= runBlocking {
    val c1 = Channel<String>()
    val c2 = Channel<String>()
    val c3 = Channel<String>()
    val c4 = Channel<Int>()
    val c5 = Channel<Double>()

    val example = ExampleClass(c1, c2, c3)

    launch {
        c1.send("Hello")
        funcB(example)
    }

    launch {
        funcA(c4)
    }

    funcC(c5)
}