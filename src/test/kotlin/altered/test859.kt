/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":5,"nChannels":5,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 5 different channels
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
package org.example.altered.test859
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel

class Producer(private val channel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            channel.send(i)
        }
    }
}

class Transformer(private val input: ReceiveChannel<Int>, private val output: Channel<Int>) {
    suspend fun transform() {
        for (value in input) {
            output.send(value * 2)
        }
    }
}

class Consumer(private val channel: ReceiveChannel<Int>) {
    suspend fun consume() {
        for (value in channel) {
            println("Consumed: $value")
        }
    }
}

fun generateNumbers(channel: Channel<Int>) = GlobalScope.launch {
    for (i in 1..5) {
        channel.send(i)
    }
}

fun main(): Unit= runBlocking {
    val firstChannel = Channel<Int>()
    val secondChannel = Channel<Int>()
    val thirdChannel = Channel<Int>()
    val fourthChannel = Channel<Int>()
    val fifthChannel = Channel<Int>()

    val producer = Producer(firstChannel)
    val transformer = Transformer(firstChannel, secondChannel)
    val secondTransformer = Transformer(secondChannel, thirdChannel)
    val consumerOne = Consumer(thirdChannel)
    val consumerTwo = Consumer(fourthChannel)
    
    launch { producer.produce() }
    launch { transformer.transform() }
    launch { secondTransformer.transform() }
    launch { consumerOne.consume() }
    launch { generateNumbers(fourthChannel) }
    launch { consumerTwo.consume() }
    
    delay(1000)
}

class RunChecker859: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}