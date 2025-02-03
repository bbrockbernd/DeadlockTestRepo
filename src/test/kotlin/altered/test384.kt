/* 
{"deadlock":true,"nFunctions":8,"nCoroutines":4,"nChannels":5,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
- 5 different channels
- 4 different coroutines
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
package org.example.altered.test384
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Alpha(val channel1: Channel<Int>, val channel3: Channel<Int>)
class Beta(val channel4: Channel<Int>, val channel5: Channel<Int>)
class Gamma(val channel2: Channel<Int>, val channel3: Channel<Int>)

fun func1(alpha: Alpha) {
    runBlocking {
        launch {
            repeat(5) {
                alpha.channel1.send(it)
                alpha.channel3.send(it)
            }
        }
    }
}

fun func2(beta: Beta) {
    runBlocking {
        launch {
            repeat(5) {
                beta.channel4.send(it)
                beta.channel5.send(it)
            }
        }
    }
}

fun func3(gamma: Gamma) {
    runBlocking {
        launch {
            repeat(5) {
                gamma.channel2.send(it)
                gamma.channel3.send(it)
            }
        }
    }
}

fun func4(alpha: Alpha) {
    runBlocking {
        launch {
            repeat(5) {
                println("func4 received from channel1: ${alpha.channel1.receive()}")
                println("func4 received from channel3: ${alpha.channel3.receive()}")
            }
        }
    }
}

fun func5(beta: Beta) {
    runBlocking {
        launch {
            repeat(5) {
                println("func5 received from channel4: ${beta.channel4.receive()}")
                println("func5 received from channel5: ${beta.channel5.receive()}")
            }
        }
    }
}

fun func6(gamma: Gamma) {
    runBlocking {
        launch {
            repeat(5) {
                println("func6 received from channel2: ${gamma.channel2.receive()}")
                println("func6 received from channel3: ${gamma.channel3.receive()}")
            }
        }
    }
}

fun func7(beta: Beta, gamma: Gamma) {
    runBlocking {
        launch {
            repeat(5) {
                beta.channel4.send(gamma.channel2.receive())
            }
        }
    }
}

fun func8(alpha: Alpha, beta: Beta) {
    runBlocking {
        launch {
            repeat(5) {
                alpha.channel1.send(beta.channel5.receive())
            }
        }
    }
}

fun main(): Unit {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()

    val alpha = Alpha(channel1, channel3)
    val beta = Beta(channel4, channel5)
    val gamma = Gamma(channel2, channel3)

    func1(alpha)
    func2(beta)
    func3(gamma)
    func4(alpha)
    func5(beta)
    func6(gamma)
    func7(beta, gamma)
    func8(alpha, beta)
}

class RunChecker384: RunCheckerBase() {
    override fun block() = main()
}