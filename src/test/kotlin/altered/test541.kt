/* 
{"deadlock":true,"nFunctions":1,"nCoroutines":3,"nChannels":2,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
- 2 different channels
- 3 different coroutines
- 2 different classes

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
package org.example.altered.test541
import org.example.altered.test541.RunChecker541.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()

    fun produce() {
        runBlocking(pool) {
            launch(pool) {
                for (i in 1..5) {
                    channel1.send(i)
                    println("Produced $i")
                }
            }
        } 
    }
}

class Consumer(val producer: Producer) {
    fun consume() {
        runBlocking(pool) {
            launch(pool) {
                for (i in 1..5) {
                    val value = producer.channel1.receive()
                    println("Consumed $value")
                    producer.channel2.send(value * 2)
                    println("Processed $value")
                }
            }
        }
    }

    fun additionalConsumption() {
        runBlocking(pool) {
            launch(pool) {
                for (i in 1..5) {
                    val value = producer.channel2.receive()
                    println("Additional consumed $value")
                }
            }
        }
    }
}

fun main(): Unit{
    val producer = Producer()
    val consumer = Consumer(producer)
    
    runBlocking(pool) {
        launch(pool) { producer.produce() }
        launch(pool) { consumer.consume() }
        launch(pool) { consumer.additionalConsumption() }
    }
}

class RunChecker541: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}