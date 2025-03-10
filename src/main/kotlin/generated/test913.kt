/* 
{"deadlock":true,"nFunctions":1,"nCoroutines":5,"nChannels":1,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
- 1 different channels
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
package org.example.generated.test913
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer(private val channel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..3) {
            println("Producing $i")
            channel.send(i)
        }
    }
}

class Consumer(private val channel: Channel<Int>) {
    suspend fun consume() {
        for (i in 1..3) {
            val value = channel.receive()
            println("Consuming $value")
        }
    }
}

class Processor(private val channel: Channel<Int>) {
    suspend fun process() {
        val value = channel.receive()
        println("Processing $value")
    }
}

fun main(): Unit= runBlocking {
    val channel = Channel<Int>()

    val producer = Producer(channel)
    val consumer = Consumer(channel)
    val processor = Processor(channel)

    launch { producer.produce() }
    launch { consumer.consume() }
    launch { processor.process() }
    launch { processor.process() }
    launch { producer.produce() }
    
    println("This will never get printed due to deadlock")
}