/* 
{"deadlock":false,"nFunctions":6,"nCoroutines":5,"nChannels":2,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 2 different channels
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
package org.example.altered.test250
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(val channel: Channel<Int>) {
    fun produce() {
        runBlocking {
            launch {
                for (i in 1..5) {
                    channel.send(i)
                }
            }
        }
    }
}

class Consumer(val channel: Channel<Int>) {
    suspend fun consume(name: String) {
        coroutineScope {
            launch {
                repeat(5) {
                    val received = channel.receive()
                    println("Consumer $name received: $received")
                }
            }
        }
    }
}

class Processor(val inputChannel: Channel<Int>, val outputChannel: Channel<Int>) {
    suspend fun process() {
        coroutineScope {
            launch {
                repeat(5) {
                    val received = inputChannel.receive()
                    outputChannel.send(received * 2)
                }
            }
        }
    }
}

fun producer1Function(channel: Channel<Int>) {
    val producer = Producer(channel)
    producer.produce()
}

fun producer2Function(channel: Channel<Int>) {
    val producer = Producer(channel)
    producer.produce()
}

suspend fun processorFunction(inputChannel: Channel<Int>, outputChannel: Channel<Int>) {
    val processor = Processor(inputChannel, outputChannel)
    processor.process()
}

fun mainFunction() {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()

    runBlocking {
        launch {
            producer1Function(channel1)
        }

        launch {
            producer2Function(channel1)
        }

        launch {
            processorFunction(channel1, channel2)
        }

        launch {
            val consumer1 = Consumer(channel2)
            consumer1.consume("1")
        }

        launch {
            val consumer2 = Consumer(channel2)
            consumer2.consume("2")
        }
    }
}

fun main(): Unit {
    mainFunction()
}

class RunChecker250: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}