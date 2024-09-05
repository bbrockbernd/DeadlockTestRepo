/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":3,"nChannels":1,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 1 different channels
- 3 different coroutines
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
package org.example.generated.test521
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun main(): Unit= runBlocking {
    val channel = Channel<Int>()

    val job1 = launch { functionOne(channel) }
    val job2 = launch { functionTwo(channel) }
    val job3 = launch { functionThree(channel) }

    functionFive()
    job1.join()
    job2.join()
    job3.join()
}

suspend fun functionOne(channel: Channel<Int>) {
    for (i in 1..5) {
        channel.send(i)
        delay(100)
    }
}

suspend fun functionTwo(channel: Channel<Int>) {
    for (i in 1..5) {
        val received = channel.receive()
        functionFour(received)
        delay(150)
    }
}

suspend fun functionThree(channel: Channel<Int>) {
    for (i in 1..5) {
        val received = channel.receive()
        println("Function Three received: $received")
        delay(200)
    }
}

fun functionFour(value: Int) {
    println("Function Four processed: $value")
}

fun functionFive() {
    println("Function Five is independent")
}