/* 
{"deadlock":true,"nFunctions":8,"nCoroutines":1,"nChannels":2,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
- 2 different channels
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
package org.example.altered.test224
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(val channel1: Channel<Int>, val channel2: Channel<String>) {
    fun produce() {
        runBlocking {
            launch {
                for (i in 1..5) {
                    channel1.send(i)
                }
            }
            launch {
                val nums = arrayOf("One", "Two", "Three", "Four", "Five")
                for (num in nums.indices) {
                    channel2.send(nums[num])
                }
            }
        }
    }
}

class Consumer1(val channel1: Channel<Int>, val channel2: Channel<String>) {
    fun consume() {
        runBlocking {
            launch {
                while (true) {
                    val value = channel1.receive()
                    println("Consumer1 received: $value")
                }
            }
            launch {
                while (true) {
                    val value = channel2.receive()
                    println("Consumer1 received: $value")
                }
            }
        }
    }
}

class Consumer2(val channel1: Channel<Int>, val channel2: Channel<String>) {
    fun consume() {
        runBlocking {
            launch {
                while (true) {
                    val value = channel1.receive()
                    println("Consumer2 received: $value")
                }
            }
            launch {
                while (true) {
                    val value = channel2.receive()
                    println("Consumer2 received: $value")
                }
            }
        }
    }
}

class Coordinator(val prod: Producer, val cons1: Consumer1, val cons2: Consumer2) {
    fun start() {
        prod.produce()
        cons1.consume()
        cons2.consume()
    }
}

class Initiator {
    private val channel1 = Channel<Int>()
    private val channel2 = Channel<String>()
    private val producer = Producer(channel1, channel2)
    private val consumer1 = Consumer1(channel1, channel2)
    private val consumer2 = Consumer2(channel1, channel2)
    private val coordinator = Coordinator(producer, consumer1, consumer2)

    fun initiate() {
        coordinator.start()
    }
}

fun main(): Unit {
    val initiator = Initiator()
    initiator.initiate()
}

class RunChecker224: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}