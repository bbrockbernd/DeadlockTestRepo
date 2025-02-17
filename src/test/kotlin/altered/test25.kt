/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":1,"nChannels":1,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 1 different channels
- 1 different coroutines
- 5 different classes

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
package org.example.altered.test25
import org.example.altered.test25.RunChecker25.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class Data(val value: Int)
class Processor(val id: Int)
class Task(val description: String)
class Result(val success: Boolean)
class Worker(val id: Int)

suspend fun produceData(channel: SendChannel<Data>) {
    val data = Data(5)
    channel.send(data)
}

suspend fun processData(channel: ReceiveChannel<Data>, task: Task): Result {
    val data = channel.receive()
    println("Processing data: ${data.value} for task: ${task.description}")
    return Result(success = true)
}

suspend fun workerFunction(channel: SendChannel<Data>, channel2: ReceiveChannel<Data>) {
    val task = Task("Important Task")
    val result = processData(channel2, task)
    println("Result: ${result.success}")
    produceData(channel)
}

fun main(): Unit = runBlocking(pool) {
    val channel = Channel<Data>()
    val channel2 = Channel<Data>()
    launch(pool) {
        produceData(channel)
        workerFunction(channel, channel2)
    }

    launch(pool) {
        workerFunction(channel2, channel)
    }
}

class RunChecker25: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}