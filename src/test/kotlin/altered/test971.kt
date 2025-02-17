/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":2,"nChannels":4,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 4 different channels
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
- lists, arrays or other datastructures
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
package org.example.altered.test971
import org.example.altered.test971.RunChecker971.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun functionA(channelA: Channel<Int>, channelB: Channel<Int>) {
    runBlocking(pool) {
        launch(pool) {
            for (i in 1..5) {
                channelA.send(i)
                println("Sent $i from A to B")
            }
            channelA.close()
        }
        launch(pool) {
            for (i in channelA) {
                channelB.send(i * 2)
                println("Sent ${i * 2} from B to C")
            }
            channelB.close()
        }
    }
}

fun functionB(channelB: Channel<Int>, channelC: Channel<Int>) {
    runBlocking(pool) {
        launch(pool) {
            for (i in channelB) {
                channelC.send(i + 3)
                println("Sent ${i + 3} from C to D")
            }
            channelC.close()
        }
    }
}

fun functionC(channelC: Channel<Int>, channelD: Channel<Int>) {
    runBlocking(pool) {
        launch(pool) {
            for (i in channelC) {
                channelD.send(i - 1)
                println("Sent ${i - 1} from D to E")
            }
            channelD.close()
        }
    }
}

fun functionD(channelD: Channel<Int>) {
    runBlocking(pool) {
        launch(pool) {
            for (i in channelD) {
                println("Received $i in E")
            }
        }
    }
}

fun main(): Unit{
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()
    val channelC = Channel<Int>()
    val channelD = Channel<Int>()

    functionA(channelA, channelB)
    functionB(channelB, channelC)
    functionC(channelC, channelD)
    functionD(channelD)
}

class RunChecker971: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}