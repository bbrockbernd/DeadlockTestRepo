/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":3,"nChannels":7,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 7 different channels
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
package org.example.altered.test182
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

fun main(): Unit= runBlocking {
    val ch1 = Channel<Int>()
    val ch2 = Channel<Int>()
    val ch3 = Channel<Int>()
    val ch4 = Channel<Int>()
    val ch5 = Channel<Int>()
    val ch6 = Channel<Int>()
    val ch7 = Channel<Int>()

    launch { coroutine1(ch1, ch2) }
    launch { coroutine2(ch3, ch4) }
    launch { coroutine3(ch5, ch6, ch7) }
}

suspend fun coroutine1(ch1: Channel<Int>, ch2: Channel<Int>) {
    function1(ch1, ch2)
    function2(ch2, ch1)
}

suspend fun coroutine2(ch3: Channel<Int>, ch4: Channel<Int>) {
    function2(ch3, ch4)
    function3(ch4, ch3)
}

suspend fun coroutine3(ch5: Channel<Int>, ch6: Channel<Int>, ch7: Channel<Int>) {
    function3(ch5, ch6)
    function4(ch6, ch7)
    function5(ch7, ch5)
}

suspend fun function1(chIn: Channel<Int>, chOut: Channel<Int>) {
    val value = chIn.receive()
    chOut.send(value)
}

suspend fun function2(chIn: Channel<Int>, chOut: Channel<Int>) {
    val value = chIn.receive()
    chOut.send(value)
}

suspend fun function3(chIn: Channel<Int>, chOut: Channel<Int>) {
    chOut.send(42)
    val value = chIn.receive()
}

suspend fun function4(chIn: Channel<Int>, chOut: Channel<Int>) {
    chOut.send(7)
    val value = chIn.receive()
}

suspend fun function5(chIn: Channel<Int>, chOut: Channel<Int>) {
    val value = chIn.receive()
    chOut.send(value + 1)
}

class RunChecker182: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}