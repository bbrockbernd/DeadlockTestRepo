/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":2,"nChannels":2,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 2 different channels
- 2 different coroutines
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
package org.example.altered.test396
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class TestClass(val ch1: Channel<Int>, val ch2: Channel<Int>)

fun function1(ch: Channel<Int>) = runBlocking {
    ch.send(1)
    // Attempt to receive without corresponding send will cause deadlock
    ch.receive()
}

fun function2(ch: Channel<Int>) = runBlocking {
    ch.send(2)
    // Attempt to receive without corresponding send will cause deadlock
    ch.receive()
}

suspend fun function3(instance: TestClass) = coroutineScope {
    launch {
        function1(instance.ch1)
    }
    function2(instance.ch2)
}

suspend fun function4(instance: TestClass) = coroutineScope {
    launch {
        function1(instance.ch2)
    }
    function2(instance.ch1)
}

fun main(): Unit= runBlocking {
    val ch1 = Channel<Int>()
    val ch2 = Channel<Int>()
    val instance = TestClass(ch1, ch2)

    launch { function3(instance) }
    launch { function4(instance) }
}

class RunChecker396: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}