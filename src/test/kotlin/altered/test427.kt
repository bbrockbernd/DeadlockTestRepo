/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":1,"nChannels":5,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 5 different channels
- 1 different coroutines
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
package org.example.altered.test427
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(val output: Channel<Int>) {
    suspend fun produce() {
        output.send(1)
        output.send(2)
        output.send(3)
    }
}

class Consumer(val input: Channel<Int>, val output: Channel<Int>) {
    suspend fun consume() {
        val item = input.receive()
        output.send(item)
    }
}

class Distributor(val input1: Channel<Int>, val input2: Channel<Int>, val output: Channel<Int>) {
    suspend fun distribute() {
        val item1 = input1.receive()
        val item2 = input2.receive()
        output.send(item1 + item2)
    }
}

class Aggregator(val input: Channel<Int>) {
    suspend fun aggregate(): Int {
        return input.receive() + input.receive()
    }
}

class DeadlockSimulator(
    val ch1: Channel<Int>,
    val ch2: Channel<Int>,
    val ch3: Channel<Int>,
    val ch4: Channel<Int>,
    val ch5: Channel<Int>
) {
    suspend fun runSimulation() = coroutineScope {
        launch {
            Producer(ch1).produce()
        }
        launch {
            Consumer(ch1, ch2).consume()
        }
        launch {
            Consumer(ch2, ch3).consume()
        }
        launch {
            Distributor(ch3, ch4, ch5).distribute()
        }
        launch {
            val result = Aggregator(ch5).aggregate()
            println("Aggregated result: $result")
        }
    }
}

fun main(): Unit= runBlocking {
    val ch1 = Channel<Int>()
    val ch2 = Channel<Int>()
    val ch3 = Channel<Int>()
    val ch4 = Channel<Int>()
    val ch5 = Channel<Int>()

    val simulator = DeadlockSimulator(ch1, ch2, ch3, ch4, ch5)
    simulator.runSimulation()
}

class RunChecker427: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}