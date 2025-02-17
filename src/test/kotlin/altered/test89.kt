/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":8,"nChannels":4,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 4 different channels
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
package org.example.altered.test89
import org.example.altered.test89.RunChecker89.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class Producer {
    val channel1 = Channel<Int>()
    val channel2 = Channel<String>()

    fun produceNumbers() = runBlocking(pool) {
        launch(pool) {
            for (i in 1..10) {
                channel1.send(i)
            }
            channel1.close()
        }
    }

    fun produceStrings() = runBlocking(pool) {
        launch(pool) {
            for (i in 1..10) {
                channel2.send("String $i")
            }
            channel2.close()
        }
    }
}

class Consumer {
    val channel3 = Channel<Double>()
    val channel4 = Channel<Char>()

    fun consumeNumbersAndProduceDoubles(channel: ReceiveChannel<Int>) = runBlocking(pool) {
        launch(pool) {
            for (number in channel) {
                val doubleValue = number.toDouble()
                channel3.send(doubleValue)
            }
            channel3.close()
        }
    }

    fun consumeStringsAndProduceChars(channel: ReceiveChannel<String>) = runBlocking(pool) {
        launch(pool) {
            for (str in channel) {
                val charValue = str.last()
                channel4.send(charValue)
            }
            channel4.close()
        }
    }
}

class Processor {
    fun processDoubles(channel: ReceiveChannel<Double>) = runBlocking(pool) {
        launch(pool) {
            for (doubleValue in channel) {
                println("Processed Double: $doubleValue")
            }
        }
    }

    fun processChars(channel: ReceiveChannel<Char>) = runBlocking(pool) {
        launch(pool) {
            for (charValue in channel) {
                println("Processed Char: $charValue")
            }
        }
    }
}

fun main(): Unit= runBlocking(pool) {
    val producer = Producer()
    val consumer = Consumer()
    val processor = Processor()

    producer.produceNumbers()
    producer.produceStrings()

    consumer.consumeNumbersAndProduceDoubles(producer.channel1)
    consumer.consumeStringsAndProduceChars(producer.channel2)

    processor.processDoubles(consumer.channel3)
    processor.processChars(consumer.channel4)

    coroutineScope {
        launch(pool) { processor.processDoubles(consumer.channel3) }
        launch(pool) { processor.processChars(consumer.channel4) }
        launch(pool) { consumer.consumeNumbersAndProduceDoubles(producer.channel1) }
        launch(pool) { consumer.consumeStringsAndProduceChars(producer.channel2) }
        launch(pool) { producer.produceNumbers() }
        launch(pool) { producer.produceStrings() }
    }
}

class RunChecker89: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}