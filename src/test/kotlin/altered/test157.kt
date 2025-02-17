/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":2,"nChannels":5,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 5 different channels
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
package org.example.altered.test157
import org.example.altered.test157.RunChecker157.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class DataProducer {
    val channelA = Channel<Int>()
    val channelB = Channel<Int>()
    
    suspend fun produceData() {
        channelA.send(1)
        val received = channelB.receive()
        println("DataProducer received $received from channelB")
    }
}

class DataConsumer {
    val channelC = Channel<Int>()
    
    suspend fun consumeData(channel: ReceiveChannel<Int>, outputChannel: SendChannel<Int>) {
        val received = channel.receive()
        outputChannel.send(received)
        println("DataConsumer received $received and sent to channelC")
    }
}

class DataProcessor {
    val channelD = Channel<Int>()
    val channelE = Channel<Int>()
    
    suspend fun processData(inputChannel: ReceiveChannel<Int>, outputChannel: SendChannel<Int>) {
        val received = inputChannel.receive()
        outputChannel.send(received)
        println("DataProcessor received $received and sent to channelE")
    }
}

fun function1(producer: DataProducer, consumer: DataConsumer) {
    runBlocking(pool) {
        producer.produceData()
        launch(pool) {
            consumer.consumeData(producer.channelA, consumer.channelC)
        }
    }
}

fun function2(consumer: DataConsumer, processor: DataProcessor) {
    runBlocking(pool) {
        launch(pool) {
            processor.processData(consumer.channelC, processor.channelD)
        }
    }
}

fun function3(producer: DataProducer, processor: DataProcessor) {
    runBlocking(pool) {
        launch(pool) {
            val received = processor.channelD.receive()
            processor.channelE.send(received)
            producer.channelB.send(received)
            println("DataProcessor sent $received back to channelB")
        }
    }
}

fun main(): Unit {
    val producer = DataProducer()
    val consumer = DataConsumer()
    val processor = DataProcessor()

    function1(producer, consumer)
    function2(consumer, processor)
    function3(producer, processor)

    // The above functions will cause a deadlock as follows:
    // - DataProducer sends 1 to channelA and awaits to receive from channelB
    // - DataConsumer tries to receive from channelA and send to channelC, which requires DataProducer to send something first
    // - DataProcessor tries to receive from channelC and send to channelD
    // - DataProcessor also tries to send to channelE and back to channelB

    // There is a cyclic dependency where each component is blocked waiting for input from a channel that can't proceed without receiving data from another blocked component
}

class RunChecker157: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}