/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":6,"nChannels":5,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 5 different channels
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
package org.example.altered.test56
import org.example.altered.test56.RunChecker56.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.*

fun sendMessages(ch1: Channel<Int>, ch2: Channel<Int>, ch3: Channel<Int>) {
    runBlocking(pool) {
        launch(pool) {
            repeat(5) {
                ch1.send(it)
            }
            ch1.close()
        }

        launch(pool) {
            for (msg in ch1) {
                ch2.send(msg * 2)
            }
            ch2.close()
        }

        launch(pool) {
            for (msg in ch2) {
                ch3.send(msg + 1)
            }
            ch3.close()
        }
    }
}

fun receiveMessages(ch3: Channel<Int>, ch4: Channel<Int>, ch5: Channel<Int>) {
    runBlocking(pool) {
        launch(pool) {
            for (msg in ch3) {
                ch4.send(msg - 1)
            }
            ch4.close()
        }

        launch(pool) {
            for (msg in ch4) {
                ch5.send(msg / 2)
            }
            ch5.close()
        }

        launch(pool) {
            for (msg in ch5) {
                println("Received: $msg")
            }
        }
    }
}

fun main(): Unit{
    val ch1 = Channel<Int>()
    val ch2 = Channel<Int>()
    val ch3 = Channel<Int>()
    val ch4 = Channel<Int>()
    val ch5 = Channel<Int>()

    runBlocking(pool) {
        launch(pool) {
            sendMessages(ch1, ch2, ch3)
        }

        launch(pool) {
            receiveMessages(ch3, ch4, ch5)
        }
    }
}

class RunChecker56: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}