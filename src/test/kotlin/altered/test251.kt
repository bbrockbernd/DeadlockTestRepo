/* 
{"deadlock":false,"nFunctions":8,"nCoroutines":7,"nChannels":8,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
- 8 different channels
- 7 different coroutines
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
package org.example.altered.test251
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Alpha(val ch: Channel<Int>)
class Beta(val ch: Channel<Int>)
class Gamma(val ch: Channel<Int>)
class Delta(val ch: Channel<Int>)
class Epsilon(val ch: Channel<Int>)

fun function1(ch: Channel<Int>) {
    runBlocking {
        ch.send(1)
    }
}

fun function2(ch1: Channel<Int>, ch2: Channel<Int>) {
    runBlocking {
        val data = ch1.receive()
        ch2.send(data + 1)
    }
}

suspend fun function3(ch: Channel<Int>) {
    ch.send(2)
}

suspend fun function4(ch1: Channel<Int>, ch2: Channel<Int>) {
    val data = ch1.receive()
    ch2.send(data + 2)
}

fun function5(alpha: Alpha, beta: Beta) {
    runBlocking {
        launch { function1(alpha.ch) }
        launch { function2(alpha.ch, beta.ch) }
    }
}

fun function6(gamma: Gamma, delta: Delta) {
    runBlocking {
        launch { function3(gamma.ch) }
        launch { function4(gamma.ch, delta.ch) }
    }
}

fun function7(epsilon: Epsilon, ch: Channel<Int>) {
    runBlocking {
        launch {
            val data = epsilon.ch.receive()
            ch.send(data + 3)
        }
    }
}

fun function8(beta: Beta, epsilon: Epsilon) {
    runBlocking {
        launch {
            val data = beta.ch.receive()
            epsilon.ch.send(data + 4)
        }
    }
}

fun main(): Unit{
    val ch1 = Channel<Int>()
    val ch2 = Channel<Int>()
    val ch3 = Channel<Int>()
    val ch4 = Channel<Int>()
    val ch5 = Channel<Int>()
    val ch6 = Channel<Int>()
    val ch7 = Channel<Int>()
    val ch8 = Channel<Int>()

    val alpha = Alpha(ch1)
    val beta = Beta(ch2)
    val gamma = Gamma(ch3)
    val delta = Delta(ch4)
    val epsilon = Epsilon(ch5)

    function5(alpha, beta)
    function6(gamma, delta)
    function7(epsilon, ch6)
    function8(beta, epsilon)

    runBlocking {
        launch {
            val data1 = ch2.receive()
            ch7.send(data1 + 5)
        }
        launch {
            val data2 = ch7.receive()
            ch8.send(data2 + 6)
        }
    }
}

class RunChecker251: RunCheckerBase() {
    override fun block() = main()
}