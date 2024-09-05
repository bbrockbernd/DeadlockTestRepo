/* 
{"deadlock":true,"nFunctions":2,"nCoroutines":6,"nChannels":7,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
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
package org.example.generated.test46
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

fun main(): Unit = runBlocking {
    val ch1 = Channel<Int>()
    val ch2 = Channel<Int>()
    val ch3 = Channel<Int>()
    val ch4 = Channel<Int>()
    val ch5 = Channel<Int>()
    val ch6 = Channel<Int>()
    val ch7 = Channel<Int>()

    launch { functionA(ch1, ch2, ch3) }
    launch { functionB(ch2, ch4, ch7) }
    launch { functionA(ch5, ch6, ch7) }
    launch { functionB(ch1, ch3, ch5) }
    launch { functionA(ch4, ch5, ch6) }
    launch { functionB(ch6, ch1, ch3) }
}

suspend fun functionA(channel1: Channel<Int>, channel2: Channel<Int>, channel3: Channel<Int>) = coroutineScope {
    launch { channel2.send(1) }
    launch { channel3.receive() }
    launch { channel1.send(1) }
}

suspend fun functionB(channel1: Channel<Int>, channel4: Channel<Int>, channel7: Channel<Int>) = coroutineScope {
    launch { channel4.send(1) }
    launch { channel7.receive() }
    launch { channel1.send(1) }
}