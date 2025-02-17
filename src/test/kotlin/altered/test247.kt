/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":4,"nChannels":6,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 6 different channels
- 4 different coroutines
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
package org.example.altered.test247
import org.example.altered.test247.RunChecker247.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class ClassA(val chan1: Channel<Int>, val chan2: Channel<Int>)

class ClassB(val chan3: Channel<Int>, val chan4: Channel<Int>)

class ClassC(val chan5: Channel<Int>)

class ClassD(val chan6: Channel<Int>)

class ClassE(val chan3: Channel<Int>, val chan4: Channel<Int>)

fun function1(a: ClassA, b: ClassB) {
    GlobalScope.launch(pool) {
        a.chan1.send(1)
        b.chan3.receive()
    }
}

fun function2(c: ClassC, d: ClassD) {
    GlobalScope.launch(pool) {
        c.chan5.send(2)
        d.chan6.receive()
    }
}

fun function3(a: ClassA, e: ClassE) {
    GlobalScope.launch(pool) {
        a.chan2.send(3)
        e.chan4.receive()
    }
}

fun function4(b: ClassB, d: ClassD, c: ClassC) {
    GlobalScope.launch(pool) {
        b.chan4.send(4)
        d.chan6.receive()
        c.chan5.receive()
    }
}

fun main(): Unit= runBlocking(pool) {
    val chan1 = Channel<Int>()
    val chan2 = Channel<Int>()
    val chan3 = Channel<Int>()
    val chan4 = Channel<Int>()
    val chan5 = Channel<Int>()
    val chan6 = Channel<Int>()

    val a = ClassA(chan1, chan2)
    val b = ClassB(chan3, chan4)
    val c = ClassC(chan5)
    val d = ClassD(chan6)
    val e = ClassE(chan3, chan4)

    function1(a, b)
    function2(c, d)
    function3(a, e)
    function4(b, d, c)

    delay(1000)
}

class RunChecker247: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}