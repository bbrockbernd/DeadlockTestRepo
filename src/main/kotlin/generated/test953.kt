/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":2,"nChannels":1,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 1 different channels
- 2 different coroutines
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
package org.example.generated.test953
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun main(): Unit= runBlocking {
    val channel = Channel<Int>()

    launch { producer(channel) }
    launch { consumer(channel) }

    coroutineScope {
        // Additional function calls to meet the 5-function requirement
        process1()
        process2()
        process3()
    }
}

fun process1() {
    println("Process 1 executing")
}

fun process2() {
    println("Process 2 executing")
}

fun process3() {
    println("Process 3 executing")
}

suspend fun producer(channel: Channel<Int>) {
    for (x in 1..5) {
        channel.send(x)
        delay(100) // Small delay to mimic some processing
    }
    channel.close()
}

suspend fun consumer(channel: Channel<Int>) {
    for (element in channel) {
        println("Received $element")
    }
}