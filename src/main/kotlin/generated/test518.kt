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
package org.example.generated.test518
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

fun startProcess(p: Process, input: Channel<Int>) = GlobalScope.launch {
    for (value in input) {
        p.inputChannel.send(value)
    }
}

fun mainProcessor(input: Channel<Int>, output: Channel<Int>) = GlobalScope.launch {
    val p1 = Process()
    val p2 = Process()
    
    startProcess(p1, input)
    GlobalScope.launch { p1.processData() }
    startProcess(p2, p1.outputChannel)
    GlobalScope.launch { p2.processData() }
    
    for (result in p2.outputChannel) {
        output.send(result)
    }
}

fun source(input: Channel<Int>) = GlobalScope.launch {
    repeat(10) {
        input.send(it)
    }
    input.close()
}

fun sink(output: Channel<Int>) = GlobalScope.launch {
    for (result in output) {
        println(result)
    }
}

fun main(): Unit= runBlocking {
    val input = Channel<Int>()
    val output = Channel<Int>()

    source(input)
    mainProcessor(input, output)
    sink(output)

    delay(2000L)
}