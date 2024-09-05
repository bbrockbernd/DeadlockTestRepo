/* 
{"deadlock":true,"nFunctions":7,"nCoroutines":2,"nChannels":6,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 6 different channels
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
package org.example.generated.test108
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

fun createChannel1(): Channel<Int> = Channel()
fun createChannel2(): Channel<Int> = Channel()
fun createChannel3(): Channel<Int> = Channel()
fun createChannel4(): Channel<Int> = Channel()
fun createChannel5(): Channel<Int> = Channel()
fun createChannel6(): Channel<Int> = Channel()

fun processChannels(ch1: Channel<Int>, ch2: Channel<Int>, ch3: Channel<Int>, ch4: Channel<Int>, ch5: Channel<Int>, ch6: Channel<Int>) = runBlocking {
    launch {
        for (x in 1..5) {
            ch1.send(x)
        }
        ch2.receive()
        ch3.send(1)
    }

    launch {
        ch1.receive()
        ch2.send(1)
        ch4.receive()
        ch5.send(1)
    }

    ch6.send(1)
    ch3.receive()
    ch4.send(1)
}

fun main(): Unit{
    val ch1 = createChannel1()
    val ch2 = createChannel2()
    val ch3 = createChannel3()
    val ch4 = createChannel4()
    val ch5 = createChannel5()
    val ch6 = createChannel6()

    processChannels(ch1, ch2, ch3, ch4, ch5, ch6)
}