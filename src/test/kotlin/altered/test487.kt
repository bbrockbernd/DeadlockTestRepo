/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":6,"nChannels":6,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 6 different channels
- 6 different coroutines
- 1 different classes

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
package org.example.altered.test487
import org.example.altered.test487.RunChecker487.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class DataProcessor(val inputChannel: Channel<Int>, val outputChannel: Channel<String>) {
    suspend fun process() {
        for (data in inputChannel) {
            outputChannel.send("Processed: $data")
        }
    }
}

fun produceNumbers(channel: Channel<Int>) = GlobalScope.launch(pool) {
    for (i in 1..5) {
        channel.send(i)
        delay(100)
    }
    channel.close()
}

fun consumeStrings(channel: Channel<String>) = GlobalScope.launch(pool) {
    for (message in channel) {
        println(message)
    }
}

fun main(): Unit = runBlocking(pool) {
    val channel1 = Channel<Int>()
    val channel2 = Channel<String>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<String>()
    val channel5 = Channel<Int>()
    val channel6 = Channel<String>()

    val processor1 = DataProcessor(channel1, channel2)
    val processor2 = DataProcessor(channel3, channel4)
    val processor3 = DataProcessor(channel5, channel6)

    coroutineScope {
        launch(pool) { processor1.process() }
        launch(pool) { processor2.process() }
        launch(pool) { processor3.process() }

        launch(pool) { produceNumbers(channel1) }
        launch(pool) { produceNumbers(channel3) }
        launch(pool) { produceNumbers(channel5) }

        launch(pool) { consumeStrings(channel2) }
        launch(pool) { consumeStrings(channel4) }
        launch(pool) { consumeStrings(channel6) }
    }
}

class RunChecker487: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}