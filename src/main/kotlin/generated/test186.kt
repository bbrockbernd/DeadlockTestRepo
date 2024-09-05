/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":3,"nChannels":8,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 8 different channels
- 3 different coroutines
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
package org.example.generated.test186
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer {
    val output: Channel<Int> = Channel(5) // Buffered channel

    suspend fun produce() {
        for (i in 1..5) {
            output.send(i)
        }
        output.close()
    }
}

class Consumer {
    val input: Channel<Int> = Channel(5) // Buffered channel

    suspend fun consume() {
        for (i in 1..5) {
            println("Consumed: ${input.receive()}")
        }
    }
}

class Controller {
    val channelA: Channel<Int> = Channel() // Unbuffered channel
    val channelB: Channel<Int> = Channel() // Unbuffered channel

    suspend fun control(producer: Producer, consumer: Consumer) {
        coroutineScope {
            launch {
                for (i in producer.output) {
                    channelA.send(i)
                }
            }
            launch {
                while (!producer.output.isClosedForReceive) {
                    val item = channelA.receive()
                    consumer.input.send(item)
                }
                consumer.input.close()
            }
        }
    }

    suspend fun extraFlow(channelC: Channel<Int>, channelD: Channel<Int>) {
        for (i in 6..10) {
            channelC.send(i)
        }
        while (!channelC.isClosedForReceive) {
            channelD.send(channelC.receive())
        }
        channelD.close()
    }
}

fun main(): Unit= runBlocking {
    val producer = Producer()
    val consumer = Consumer()
    val controller = Controller()
    val channelE: Channel<Int> = Channel()
    val channelF: Channel<Int> = Channel()

    launch { producer.produce() }
    launch { consumer.consume() }
    launch {
        controller.control(producer, consumer)
    }
    launch {
        controller.extraFlow(channelE, channelF)
    }
}