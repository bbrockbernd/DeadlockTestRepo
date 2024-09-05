/* 
{"deadlock":false,"nFunctions":1,"nCoroutines":1,"nChannels":4,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
- 4 different channels
- 1 different coroutines
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
package org.example.generated.test641
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ChannelHandler(private val ch1: Channel<Int>, private val ch2: Channel<Int>, private val ch3: Channel<Int>, private val ch4: Channel<Int>) {

    suspend fun handleChannels() {
        val value1 = ch1.receive()
        val value2 = ch2.receive()
        ch3.send(value1 + value2)
        ch4.send(value1 - value2)
    }
}

fun initChannels(): List<Channel<Int>> {
    val ch1 = Channel<Int>()
    val ch2 = Channel<Int>()
    val ch3 = Channel<Int>()
    val ch4 = Channel<Int>()
    return listOf(ch1, ch2, ch3, ch4)
}

fun main(): Unit= runBlocking {
    val (ch1, ch2, ch3, ch4) = initChannels()
    val handler = ChannelHandler(ch1, ch2, ch3, ch4)
    launch {
        ch1.send(3)
        ch2.send(2)
        handler.handleChannels()
    }
    println("ch3: ${ch3.receive()}")
    println("ch4: ${ch4.receive()}")
}