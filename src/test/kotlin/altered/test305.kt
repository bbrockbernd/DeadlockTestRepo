/* 
{"deadlock":true,"nFunctions":8,"nCoroutines":7,"nChannels":8,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
- 8 different channels
- 7 different coroutines
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
package org.example.altered.test305
import org.example.altered.test305.RunChecker305.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Class1(val ch1: Channel<Int>, val ch2: Channel<Int>)

class Class2(val ch3: Channel<Int>, val ch4: Channel<Int>)

class Class3(val ch5: Channel<Int>, val ch6: Channel<Int>, val ch7: Channel<Int>, val ch8: Channel<Int>)

fun func1(c1: Class1) = runBlocking(pool) {
    launch(pool) { c1.ch1.send(1); c1.ch2.receive() }
    func2(c1)
}

fun func2(c1: Class1) = runBlocking(pool) {
    launch(pool) { c1.ch2.send(2); c1.ch1.receive() }
    func3(Class2(c1.ch1, c1.ch2))
}

fun func3(c2: Class2) = runBlocking(pool) {
    launch(pool) { c2.ch3.send(3); c2.ch4.receive() }
    func4(c2)
}

fun func4(c2: Class2) = runBlocking(pool) {
    launch(pool) { c2.ch4.send(4); c2.ch3.receive() }
    func5(Class3(c2.ch3, c2.ch4, Channel(), Channel()))
}

fun func5(c3: Class3) = runBlocking(pool) {
    launch(pool) { c3.ch5.send(5); c3.ch6.receive() }
    launch(pool) { c3.ch7.send(6); c3.ch8.receive() }
    func6(c3)
}

fun func6(c3: Class3) = runBlocking(pool) {
    launch(pool) { c3.ch6.send(7); c3.ch5.receive() }
    launch(pool) { c3.ch8.send(8); c3.ch7.receive() }
    func7(c3)
}

fun func7(c3: Class3) = runBlocking(pool) {
    launch(pool) { c3.ch5.send(9); c3.ch6.receive() }
    launch(pool) { c3.ch7.send(10); c3.ch8.receive() }
    func8(c3)
}

fun func8(c3: Class3) = runBlocking(pool) {
    launch(pool) { c3.ch6.send(11); c3.ch5.receive() }
    launch(pool) { c3.ch8.send(12); c3.ch7.receive() }
}

fun main(): Unit = runBlocking<Unit> {
    val ch1 = Channel<Int>()
    val ch2 = Channel<Int>()
    func1(Class1(ch1, ch2))
}

class RunChecker305: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}