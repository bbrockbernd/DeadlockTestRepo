/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":4,"nChannels":1,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
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
package org.example.generated.test635
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun produce(channel: Channel<Int>) {
    GlobalScope.launch {
        repeat(4) {
            channel.send(it)
        }
    }
}

fun consume(channel: Channel<Int>) {
    GlobalScope.launch {
        repeat(4) {
            println(channel.receive())
        }
    }
}

fun main(): Unit= runBlocking {
    val channel = Channel<Int>(1)

    val producer1 = launch { produce(channel) }
    val producer2 = launch { produce(channel) }
    val consumer1 = launch { consume(channel) }
    val consumer2 = launch { consume(channel) }

    producer1.join()
    producer2.join()
    consumer1.join()
    consumer2.join()
}