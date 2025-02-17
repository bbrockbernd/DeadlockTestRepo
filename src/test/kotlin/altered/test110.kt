/* 
{"deadlock":true,"nFunctions":6,"nCoroutines":3,"nChannels":7,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 7 different channels
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
package org.example.altered.test110
import org.example.altered.test110.RunChecker110.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class A(val channel: Channel<Int>)
class B(val channel: Channel<Double>)
class C(val channel: Channel<String>)
class D(val channel: Channel<Char>)
class E(val channel: Channel<Boolean>)

fun function1(a: A, b: B) {
    runBlocking(pool) {
        launch(pool) {
            for (i in 1..5) {
                a.channel.send(i)
                println("Sent integer $i to A's channel")
            }
        }
        launch(pool) {
            for (j in 1..5) {
                val received = b.channel.receive()
                println("Received double $received from B's channel")
            }
        }
    }
}

fun function2(c: C) {
    runBlocking(pool) {
        launch(pool) {
            for (k in 1..5) {
                c.channel.send("Message $k")
                println("Sent message $k to C's channel")
            }
        }
    }
}

fun function3(c: C, d: D) {
    runBlocking(pool) {
        launch(pool) {
            for (l in 1..5) {
                val received = c.channel.receive()
                println("Received string $received from C's channel")
                d.channel.send(received[0])
                println("Sent char ${received[0]} to D's channel")
            }
        }
    }
}

fun function4(d: D, e: E) {
    runBlocking(pool) {
        launch(pool) {
            for (m in 1..5) {
                val received = d.channel.receive()
                println("Received char $received from D's channel")
                e.channel.send(received.isUpperCase())
                println("Sent boolean ${received.isUpperCase()} to E's channel")
            }
        }
    }
}

fun function5(e: E) {
    runBlocking(pool) {
        launch(pool) {
            for (n in 1..5) {
                val received = e.channel.receive()
                println("Received boolean $received from E's channel")
            }
        }
    }
}

fun function6(a: A, b: B) {
    runBlocking(pool) {
        launch(pool) {
            for (o in 1..5) {
                val intReceived = a.channel.receive()
                println("Received integer $intReceived from A's channel")
                b.channel.send(intReceived.toDouble())
                println("Sent double ${intReceived.toDouble()} to B's channel")
            }
        }
    }
}

fun main(): Unit {
    val aChannel = Channel<Int>()
    val bChannel = Channel<Double>()
    val cChannel = Channel<String>()
    val dChannel = Channel<Char>()
    val eChannel = Channel<Boolean>()

    val a = A(aChannel)
    val b = B(bChannel)
    val c = C(cChannel)
    val d = D(dChannel)
    val e = E(eChannel)

    function1(a, b)
    function2(c)
    function3(c, d)
    function4(d, e)
    function5(e)
    function6(a, b)
}

class RunChecker110: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}