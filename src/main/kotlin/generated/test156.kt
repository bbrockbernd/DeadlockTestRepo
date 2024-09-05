/* 
{"deadlock":true,"nFunctions":2,"nCoroutines":5,"nChannels":8,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 8 different channels
- 5 different coroutines
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
package org.example.generated.test156
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(val channels: List<Channel<Int>>) {
    suspend fun produce() {
        repeat(5) {
            channels[it % channels.size].send(it)
        }
    }
}

class Consumer(val channels: List<Channel<Int>>) {
    suspend fun consume() {
        repeat(5) {
            println(channels[it % channels.size].receive())
        }
    }
}

fun main(): Unit = runBlocking {
    val ch1 = Channel<Int>()
    val ch2 = Channel<Int>()
    val ch3 = Channel<Int>()
    val ch4 = Channel<Int>()
    val ch5 = Channel<Int>()
    val ch6 = Channel<Int>()
    val ch7 = Channel<Int>()
    val ch8 = Channel<Int>()

    val producer1 = Producer(listOf(ch1, ch2, ch3, ch4))
    val producer2 = Producer(listOf(ch5, ch6, ch7, ch8))
    val consumer1 = Consumer(listOf(ch1, ch2, ch5, ch6))
    val consumer2 = Consumer(listOf(ch3, ch4, ch7, ch8))

    coroutineScope {
        launch { producer1.produce() }
        launch { producer2.produce() }
        launch { consumer1.consume() }
        launch { consumer2.consume() }
        launch {
            ch1.send(10)
            println(ch8.receive())
        }
    }
}