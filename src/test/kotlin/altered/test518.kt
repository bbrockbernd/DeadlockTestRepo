/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":5,"nChannels":5,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 5 different channels
- 5 different coroutines
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
package org.example.altered.test518
import org.example.altered.test518.RunChecker518.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Process {
    val inputChannel = Channel<Int>()
    val outputChannel = Channel<Int>()
    
    suspend fun processData() {
        for (value in inputChannel) {
            outputChannel.send(value * 2)
        }
    }
}

fun startProcess(p: Process, input: Channel<Int>) = GlobalScope.launch(pool) {
    for (value in input) {
        p.inputChannel.send(value)
    }
}

fun mainProcessor(input: Channel<Int>, output: Channel<Int>) = GlobalScope.launch(pool) {
    val p1 = Process()
    val p2 = Process()
    
    startProcess(p1, input)
    GlobalScope.launch(pool) { p1.processData() }
    startProcess(p2, p1.outputChannel)
    GlobalScope.launch(pool) { p2.processData() }
    
    for (result in p2.outputChannel) {
        output.send(result)
    }
}

fun source(input: Channel<Int>) = GlobalScope.launch(pool) {
    repeat(10) {
        input.send(it)
    }
    input.close()
}

fun sink(output: Channel<Int>) = GlobalScope.launch(pool) {
    for (result in output) {
        println(result)
    }
}

fun main(): Unit= runBlocking(pool) {
    val input = Channel<Int>()
    val output = Channel<Int>()

    source(input)
    mainProcessor(input, output)
    sink(output)

    delay(2000L)
}

class RunChecker518: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}