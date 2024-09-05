/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":8,"nChannels":6,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 6 different channels
- 8 different coroutines
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
package org.example.generated.test113
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class Producer(private val outputChannel: SendChannel<Int>) {
    suspend fun produce() {
        repeat(10) {
            outputChannel.send(it)
        }
        outputChannel.close()
    }
}

class Consumer(private val inputChannel: ReceiveChannel<Int>) {
    suspend fun consume() {
        for (item in inputChannel) {
            println("Consumed: $item")
        }
    }
}

class Processor(
    private val inputChannelA: ReceiveChannel<Int>,
    private val inputChannelB: ReceiveChannel<Int>,
    private val outputChannelA: SendChannel<Int>,
    private val outputChannelB: SendChannel<Int>
) {
    suspend fun process() {
        coroutineScope {
            launch {
                for (item in inputChannelA) {
                    outputChannelA.send(item * 2)
                }
                outputChannelA.close()
            }
            launch {
                for (item in inputChannelB) {
                    outputChannelB.send(item * 3)
                }
                outputChannelB.close()
            }
        }
    }
}

fun main(): Unit= runBlocking {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()
    val channelC = Channel<Int>()
    val channelD = Channel<Int>()
    val channelE = Channel<Int>(2)
    val channelF = Channel<Int>(2)
    
    val producerA = Producer(channelA)
    val producerB = Producer(channelB)
    
    val processorA = Processor(channelA, channelB, channelC, channelD)
    
    val consumerA = Consumer(channelC)
    val consumerB = Consumer(channelD)
    val consumerC = Consumer(channelE)
    val consumerD = Consumer(channelF)
    
    launch { producerA.produce() }
    launch { producerB.produce() }
    
    launch { processorA.process() }
    
    launch {
        for (item in channelD) {
            channelE.send(item + 5)
        }
        channelE.close()
    }
    
    launch {
        for (item in channelC) {
            channelF.send(item + 10)
        }
        channelF.close()
    }
    
    launch { consumerA.consume() }
    launch { consumerB.consume() }
    launch { consumerC.consume() }
    launch { consumerD.consume() }
}