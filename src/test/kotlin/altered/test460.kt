/* 
{"deadlock":true,"nFunctions":6,"nCoroutines":5,"nChannels":2,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 2 different channels
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
package org.example.altered.test460
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class TaskProcessor(val inputChannel: Channel<Int>, val outputChannel: Channel<Int>) {
    suspend fun processTasks() {
        for (task in inputChannel) {
            outputChannel.send(task * 2)
        }
    }
}

class ChannelHandler(val inputChannel: Channel<Int>, val outputChannel: Channel<Int>) {
    suspend fun handleInput() {
        for (i in 1..5) {
            inputChannel.send(i)
        }
    }
    
    suspend fun handleOutput() {
        for (o in outputChannel) {
            println("Processed output: $o")
        }
    }
}

class Executor(val processor: TaskProcessor, val handler: ChannelHandler) {
    fun executeTasks() = runBlocking {
        launch { handler.handleInput() }
        launch { processor.processTasks() }
        launch { handler.handleOutput() }
    }
}

fun createChannels(): Pair<Channel<Int>, Channel<Int>> {
    val inputChannel = Channel<Int>()
    val outputChannel = Channel<Int>()
    return Pair(inputChannel, outputChannel)
}

fun createProcessor(inputChannel: Channel<Int>, outputChannel: Channel<Int>): TaskProcessor {
    return TaskProcessor(inputChannel, outputChannel)
}

fun createHandler(inputChannel: Channel<Int>, outputChannel: Channel<Int>): ChannelHandler {
    return ChannelHandler(inputChannel, outputChannel)
}

fun main(): Unit{
    val (inputChannel, outputChannel) = createChannels()
    val processor = createProcessor(inputChannel, outputChannel)
    val handler = createHandler(inputChannel, outputChannel)
    val executor = Executor(processor, handler)
    
    runBlocking {
        val job1 = launch { executor.executeTasks() }
        val job2 = launch { handler.handleInput() }
        val job3 = launch { processor.processTasks() }
        val job4 = launch { handler.handleOutput() }
        val job5 = launch { executor.executeTasks() }
    }
}

class RunChecker460: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}