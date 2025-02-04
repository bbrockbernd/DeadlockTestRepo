/* 
{"deadlock":true,"nFunctions":7,"nCoroutines":3,"nChannels":8,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 8 different channels
- 3 different coroutines
- 5 different classes

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
package org.example.altered.test123
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun main(): Unit = runBlocking {
    val c1 = Channel<Int>()
    val c2 = Channel<Int>()
    val c3 = Channel<Int>()
    val c4 = Channel<Int>()
    val c5 = Channel<Int>()
    val c6 = Channel<Int>()
    val c7 = Channel<Int>()
    val c8 = Channel<Int>()

    launch { function1(c1, c5, c8) }
    launch { function2(c2, c6) }
    launch { function3(c3, c7) }

    function4(c1, c2, c3)
    function5(c4, c5, c6)
    function6(c4, c7)
    function7(c8)
}

suspend fun function1(c1: Channel<Int>, c5: Channel<Int>, c8: Channel<Int>) {
    c1.send(1)
    c5.send(c8.receive())
}

suspend fun function2(c2: Channel<Int>, c6: Channel<Int>) {
    c2.send(c6.receive())
}

suspend fun function3(c3: Channel<Int>, c7: Channel<Int>) {
    c3.send(c7.receive())
}

suspend fun function4(c1: Channel<Int>, c2: Channel<Int>, c3: Channel<Int>) {
    c2.send(c1.receive())
    c3.send(c1.receive())
}

suspend fun function5(c4: Channel<Int>, c5: Channel<Int>, c6: Channel<Int>) {
    c5.send(c4.receive())
    c6.send(c5.receive())
}

suspend fun function6(c4: Channel<Int>, c7: Channel<Int>) {
    c7.send(c4.receive())
}

suspend fun function7(c8: Channel<Int>) {
    c8.send(0)
}

class Class1 {
    val c1 = Channel<Int>()
}

class Class2 {
    val c2 = Channel<Int>()
}

class Class3 {
    val c3 = Channel<Int>()
}

class Class4 {
    val c4 = Channel<Int>()
}

class Class5 {
    val c5 = Channel<Int>()
}

class RunChecker123: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}