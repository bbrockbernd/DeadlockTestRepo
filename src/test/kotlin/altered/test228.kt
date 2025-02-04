/* 
{"deadlock":true,"nFunctions":6,"nCoroutines":6,"nChannels":7,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 7 different channels
- 6 different coroutines
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
package org.example.altered.test228
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun firstChannel(): Channel<Int> = Channel()

fun secondChannel(): Channel<Int> = Channel()

fun thirdChannel(): Channel<Int> = Channel()

fun fourthChannel(): Channel<Int> = Channel()

fun fifthChannel(): Channel<Int> = Channel()

fun sixthChannel(): Channel<Int> = Channel()

fun seventhChannel(): Channel<Int> = Channel()

fun main(): Unit= runBlocking {
    val ch1 = firstChannel()
    val ch2 = secondChannel()
    val ch3 = thirdChannel()
    val ch4 = fourthChannel()
    val ch5 = fifthChannel()
    val ch6 = sixthChannel()
    val ch7 = seventhChannel()

    launch { coroutineA(ch1, ch2) }
    launch { coroutineB(ch2, ch3) }
    launch { coroutineC(ch3, ch4) }
    launch { coroutineD(ch4, ch5) }
    launch { coroutineE(ch5, ch6) }
    launch { coroutineF(ch6, ch7, ch1) }
}

suspend fun coroutineA(ch1: Channel<Int>, ch2: Channel<Int>) {
    ch1.send(1)
    ch2.receive()
}

suspend fun coroutineB(ch2: Channel<Int>, ch3: Channel<Int>) {
    ch2.send(2)
    ch3.receive()
}

suspend fun coroutineC(ch3: Channel<Int>, ch4: Channel<Int>) {
    ch3.send(3)
    ch4.receive()
}

suspend fun coroutineD(ch4: Channel<Int>, ch5: Channel<Int>) {
    ch4.send(4)
    ch5.receive()
}

suspend fun coroutineE(ch5: Channel<Int>, ch6: Channel<Int>) {
    ch5.send(5)
    ch6.receive()
}

suspend fun coroutineF(ch6: Channel<Int>, ch7: Channel<Int>, ch1: Channel<Int>) {
    ch6.send(6)
    ch7.receive()
    ch1.receive()  // Deadlock situation
}

class RunChecker228: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}