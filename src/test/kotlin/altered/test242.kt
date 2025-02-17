/* 
{"deadlock":true,"nFunctions":8,"nCoroutines":5,"nChannels":5,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
- 5 different channels
- 5 different coroutines
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
package org.example.altered.test242
import org.example.altered.test242.RunChecker242.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA(val ch1: Channel<Int>, val ch2: Channel<Int>)
class ClassB(val ch3: Channel<Int>, val ch4: Channel<Int>)
class ClassC(val ch5: Channel<Int>)

fun function1(a: ClassA) = runBlocking(pool) {
    launch(pool) {
        for (i in 1..5) {
            a.ch1.send(i)
        }
    }
}

fun function2(a: ClassA) = runBlocking(pool) {
    launch(pool) {
        repeat(5) {
            println("Received in function2: ${a.ch1.receive()}")
        }
    }
}

fun function3(b: ClassB) = runBlocking(pool) {
    launch(pool) {
        for (i in 6..10) {
            b.ch3.send(i)
        }
    }
}

fun function4(b: ClassB) = runBlocking(pool) {
    launch(pool) {
        repeat(5) {
            println("Received in function4: ${b.ch3.receive()}")
        }
    }
}

fun function5(c: ClassC) = runBlocking(pool) {
    launch(pool) {
        c.ch5.send(11)
    }
}

fun function6(c: ClassC) = runBlocking(pool) {
    launch(pool) {
        println("Received in function6: ${c.ch5.receive()}")
    }
}

fun function7(ch5: Channel<Int>, a: ClassA, b: ClassB) = runBlocking(pool) {
    launch(pool) {
        for (i in 1..3) {
            a.ch2.send(i + 10)
        }
        val receivedA = a.ch2.receive()
        b.ch4.send(receivedA)
    }
}

fun function8(ch5: Channel<Int>, b: ClassB, c: ClassC) = runBlocking(pool) {
    launch(pool) {
        val receivedB = b.ch4.receive()
        c.ch5.send(receivedB)
        println("Forwarded in function8: ${ch5.receive()}")
    }
}

val ch1 = Channel<Int>()
val ch2 = Channel<Int>()
val ch3 = Channel<Int>()
val ch4 = Channel<Int>()
val ch5 = Channel<Int>()

val a = ClassA(ch1, ch2)
val b = ClassB(ch3, ch4)
val c = ClassC(ch5)

fun main(): Unit= runBlocking<Unit> {
    launch(pool) {
        function1(a)
    }
    launch(pool) {
        function2(a)
    }
    launch(pool) {
        function3(b)
    }
    launch(pool) {
        function4(b)
    }
    launch(pool) {
        function5(c)
    }
    launch(pool) {
        function6(c)
    }
    launch(pool) {
        function7(ch5, a, b)
    }
    launch(pool) {
        function8(ch5, b, c)
    }
}

class RunChecker242: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}