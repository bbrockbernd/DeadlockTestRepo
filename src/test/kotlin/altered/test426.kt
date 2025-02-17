/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":2,"nChannels":6,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 6 different channels
- 2 different coroutines
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
package org.example.altered.test426
import org.example.altered.test426.RunChecker426.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Producer {
    val ch1 = Channel<Int>(3)
    val ch2 = Channel<Int>(3)

    suspend fun produce() {
        for (i in 1..3) {
            ch1.send(i)
            ch2.send(i + 10)
        }
        ch1.close()
        ch2.close()
    }
}

class Processor {
    val ch3 = Channel<Int>(3)
    val ch4 = Channel<Int>(3)

    suspend fun process(input1: Channel<Int>, input2: Channel<Int>) {
        for (i in 1..3) {
            ch3.send(input1.receive() * 2)
            ch4.send(input2.receive() * 2)
        }
        ch3.close()
        ch4.close()
    }
}

class Consumer {
    val ch5 = Channel<String>(3)
    val ch6 = Channel<String>(3)

    suspend fun consume(input3: Channel<Int>, input4: Channel<Int>) {
        for (i in 1..3) {
            ch5.send("Processed: ${input3.receive()}")
            ch6.send("Processed: ${input4.receive()}")
        }
        ch5.close()
        ch6.close()
    }

    fun runBlockingTest() = runBlocking(pool) {
        val producer = Producer()
        val processor = Processor()
        val consumer = Consumer()

        coroutineScope {
            launch(pool) { producer.produce() }
            launch(pool) {
                processor.process(producer.ch1, producer.ch2)
                consumer.consume(processor.ch3, processor.ch4)
                
                for (i in 1..3) {
                    println(consumer.ch5.receive())
                    println(consumer.ch6.receive())
                }
            }
        }
    }
}

fun main(): Unit{
    val consumer = Consumer()
    consumer.runBlockingTest()
}

class RunChecker426: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}