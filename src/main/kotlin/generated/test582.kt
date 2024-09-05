/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":5,"nChannels":4,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 4 different channels
- 5 different coroutines
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
package org.example.generated.test582
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

val channel1 = Channel<Int>()
val channel2 = Channel<Int>()
val channel3 = Channel<Int>()
val channel4 = Channel<Int>()

suspend fun function1() {
    channel1.send(1)
    channel2.receive()
}

suspend fun function2() {
    channel2.send(2)
    channel3.receive()
}

suspend fun function3() {
    channel3.send(3)
    channel4.receive()
}

suspend fun function4() {
    channel4.send(4)
    channel1.receive()
}

fun main(): Unit= runBlocking {
    launch { function1() }
    launch { function2() }
    launch { function3() }
    launch { function4() }
    launch {
        delay(1000)
        println("Deadlock occurred")
    }
}