/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":8,"nChannels":8,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 8 different channels
- 8 different coroutines
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
package org.example.altered.test93
import org.example.altered.test93.RunChecker93.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

fun functionOne(chan1: Channel<Int>, chan2: Channel<Int>, chan3: Channel<String>, chan4: Channel<String>) = runBlocking(pool) {
    launch(pool) {
        repeat(5) {
            chan1.send(it)
            println("Sent $it to chan1")
        }
        chan1.close()
    }

    launch(pool) {
        for (v in chan1) {
            chan2.send(v + 10)
            println("Sent ${v + 10} to chan2")
        }
        chan2.close()
    }

    launch(pool) {
        repeat(5) {
            chan3.send("one$it")
            println("Sent one$it to chan3")
        }
        chan3.close()
    }

    launch(pool) {
        for (v in chan3) {
            chan4.send(v + "X")
            println("Sent ${v}X to chan4")
        }
        chan4.close()
    }
}

fun functionTwo(chan5: Channel<Int>, chan6: Channel<Int>, chan7: Channel<String>, chan8: Channel<String>) = runBlocking(pool) {
    launch(pool) {
        repeat(5) {
            chan5.send(it)
            println("Sent $it to chan5")
        }
        chan5.close()
    }

    launch(pool) {
        for (v in chan5) {
            chan6.send(v * 2)
            println("Sent ${v * 2} to chan6")
        }
        chan6.close()
    }

    launch(pool) {
        repeat(5) {
            chan7.send("two$it")
            println("Sent two$it to chan7")
        }
        chan7.close()
    }

    launch(pool) {
        for (v in chan7) {
            chan8.send(v + "Y")
            println("Sent ${v}Y to chan8")
        }
        chan8.close()
    }
}

fun main(): Unit = runBlocking(pool) {
    val chan1 = Channel<Int>()
    val chan2 = Channel<Int>()
    val chan3 = Channel<String>()
    val chan4 = Channel<String>()
    functionOne(chan1, chan2, chan3, chan4)

    val chan5 = Channel<Int>()
    val chan6 = Channel<Int>()
    val chan7 = Channel<String>()
    val chan8 = Channel<String>()
    functionTwo(chan5, chan6, chan7, chan8)
}

class RunChecker93: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}