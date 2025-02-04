/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":5,"nChannels":3,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 3 different channels
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
package org.example.altered.test381
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class ProducerA(private val channel: Channel<Int>) {
    suspend fun produce() {
        repeat(5) {
            delay(100)
            channel.send(it)
        }
        channel.close()
    }
}

class ProducerB(private val channel: Channel<String>) {
    suspend fun produce() {
        repeat(5) {
            delay(150)
            channel.send("Message $it")
        }
        channel.close()
    }
}

class Consumer(private val intChannel: Channel<Int>, private val stringChannel: Channel<String>, private val resultChannel: Channel<Pair<Int, String>>) {
    suspend fun consume() {
        coroutineScope {
            val intList = mutableListOf<Int>()
            val stringList = mutableListOf<String>()

            launch {
                for (elem in intChannel) {
                    intList.add(elem)
                }
            }

            launch {
                for (elem in stringChannel) {
                    stringList.add(elem)
                }
            }

            launch {
                delay(1000)  // Simulate some processing time
                for (i in intList.indices) {
                    resultChannel.send(Pair(intList[i], stringList[i]))
                }
                resultChannel.close()
            }
        }
    }
}

fun main(): Unit= runBlocking {
    val intChannel = Channel<Int>()
    val stringChannel = Channel<String>()
    val resultChannel = Channel<Pair<Int, String>>()

    val producerA = ProducerA(intChannel)
    val producerB = ProducerB(stringChannel)
    val consumer = Consumer(intChannel, stringChannel, resultChannel)

    launch { producerA.produce() }
    launch { producerB.produce() }
    launch { consumer.consume() }

    for (result in resultChannel) {
        println("Received result: $result")
    }
}

class RunChecker381: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}