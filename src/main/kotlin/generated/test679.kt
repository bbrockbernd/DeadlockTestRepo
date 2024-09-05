/* 
{"deadlock":true,"nFunctions":1,"nCoroutines":3,"nChannels":5,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
- 5 different channels
- 3 different coroutines
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
package org.example.generated.test679
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class FirstClass(val ch1: Channel<Int>, val ch2: Channel<Int>) {
    suspend fun sendReceiveInOrder() {
        ch1.send(1)
        ch2.receive()
    }
}

class SecondClass(val ch3: Channel<Int>, val ch4: Channel<Int>) {
    suspend fun sendReceiveOutOfOrder() {
        ch4.send(2)
        ch3.receive()
    }
}

class ThirdClass(val ch1: Channel<Int>, val ch3: Channel<Int>, val ch5: Channel<Int>) {
    suspend fun relayData() {
        val value = ch1.receive()
        ch5.send(value)
        ch3.send(value)
    }
}

fun main(): Unit= runBlocking {
    val ch1 = Channel<Int>()
    val ch2 = Channel<Int>()
    val ch3 = Channel<Int>()
    val ch4 = Channel<Int>()
    val ch5 = Channel<Int>()

    val firstClass = FirstClass(ch1, ch2)
    val secondClass = SecondClass(ch3, ch4)
    val thirdClass = ThirdClass(ch1, ch3, ch5)

    launch { firstClass.sendReceiveInOrder() }
    launch { secondClass.sendReceiveOutOfOrder() }
    launch { thirdClass.relayData() }
}