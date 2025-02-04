/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":3,"nChannels":7,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 7 different channels
- 3 different coroutines
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
package org.example.altered.test402
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class Worker(val name: String, val inputChannel: ReceiveChannel<Int>, val outputChannel: SendChannel<Int>) {
    suspend fun process() {
        for (i in inputChannel) {
            outputChannel.send(i * 2) // Simple processing logic
        }
    }
}

fun CoroutineScope.worker1(input: ReceiveChannel<Int>, output: SendChannel<Int>) = launch {
    for (i in input) {
        output.send(i + 1)
    }
}

fun CoroutineScope.worker2(input: ReceiveChannel<Int>, output: SendChannel<Int>) = launch {
    for (i in input) {
        output.send(i * 3)
    }
}

fun CoroutineScope.worker3(input: ReceiveChannel<Int>, additionalInput: ReceiveChannel<Int>, output: SendChannel<Int>) = launch {
    for (i in input) {
        output.send(i + additionalInput.receive())
    }
}

fun createChannels(): List<Channel<Int>> {
    val channels = mutableListOf<Channel<Int>>()
    repeat(7) {
        channels.add(Channel())
    }
    return channels
}

fun main(): Unit= runBlocking {
    val channels = createChannels()

    worker1(channels[0], channels[1])
    worker2(channels[2], channels[3])
    
    val worker = Worker("CustomWorker", channels[4], channels[5])
    launch {
        worker.process()
    }

    worker3(channels[1], channels[3], channels[6])

    // Send values to start the processing
    launch {
        channels[0].send(1)
        channels[2].send(2)
        channels[4].send(3)

        // Closing the channels indicating no more data
        channels[0].close()
        channels[2].close()
        channels[4].close()
    }

    // Printing the final output
    launch {
        for (result in channels[6]) {
            println("Final result: $result")
        }
    }

    // Keeping the main coroutine alive to see the printed results
    delay(2000L)
}

class RunChecker402: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}