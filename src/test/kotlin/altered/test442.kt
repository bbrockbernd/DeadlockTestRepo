/* 
{"deadlock":true,"nFunctions":7,"nCoroutines":7,"nChannels":1,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 1 different channels
- 7 different coroutines
- 4 different classes

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
package org.example.altered.test442
import org.example.altered.test442.RunChecker442.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class Producer(val channel: Channel<Int>) {
    suspend fun produce() {
        for (i in 1..5) {
            println("Producing $i")
            channel.send(i)
        }
    }
}

class Consumer(val channel: Channel<Int>) {
    suspend fun consume() {
        repeat(5) {
            val received = channel.receive()
            println("Consuming $received")
        }
    }
}

class Middleman(val channel: Channel<Int>) {
    suspend fun mediate() {
        val tempChannel = Channel<Int>()
        coroutineScope {
            launch(pool) {
                repeat(5) {
                    val value = channel.receive()
                    println("Mediating $value")
                    tempChannel.send(value)
                }
            }
            launch(pool) {
                repeat(5) {
                    val value = tempChannel.receive()
                    println("Mediated $value")
                    channel.send(value)
                }
            }
        }
    }
}

class Controller(val channel: Channel<Int>) {
    suspend fun control() {
        repeat(5) {
            val value = channel.receive()
            println("Controlling $value")
        }
    }
}

fun producerFunc(producer: Producer) = runBlocking(pool) {
    producer.produce()
}

fun consumerFunc(consumer: Consumer) = runBlocking(pool) {
    consumer.consume()
}

fun middlemanFunc(middleman: Middleman) = runBlocking(pool) {
    middleman.mediate()
}

fun controllerFunc(controller: Controller) = runBlocking(pool) {
    controller.control()
}

fun main(): Unit = runBlocking(pool) {
    val channel = Channel<Int>()
    val producer = Producer(channel)
    val consumer = Consumer(channel)
    val middleman = Middleman(channel)
    val controller = Controller(channel)

    launch(pool) { producerFunc(producer) }
    launch(pool) { middlemanFunc(middleman) }
    launch(pool) { controllerFunc(controller) }
    launch(pool) { consumerFunc(consumer) }

    delay(2000)
}

class RunChecker442: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}