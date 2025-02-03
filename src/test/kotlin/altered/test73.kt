/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":2,"nChannels":2,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 2 different channels
- 2 different coroutines
- 4 different classes

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
package org.example.altered.test73
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.launch

class Processor1 {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    suspend fun processA() {
        channel1.send(1)
        channel2.receive()
    }
}

class Processor2(val p1: Processor1) {
    suspend fun processB() {
        p1.channel2.send(2)
        p1.channel1.receive()
    }
}

class Processor3(val p1: Processor1, val p2: Processor2) {
    suspend fun initiateProcesses() {
        val job1 = launch {
            p1.processA()
        }
        val job2 = launch {
            p2.processB()
        }
    }
}

fun mainTask() {
    val p1 = Processor1()
    val p2 = Processor2(p1)
    val p3 = Processor3(p1, p2)
    runBlocking {
        p3.initiateProcesses()
    }
}

fun main(): Unit{
    mainTask()
}

class RunChecker73: RunCheckerBase() {
    override fun block() = main()
}