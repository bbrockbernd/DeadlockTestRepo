/* 
{"deadlock":false,"nFunctions":6,"nCoroutines":7,"nChannels":2,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 2 different channels
- 7 different coroutines
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
package org.example.altered.test444
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class DataProcessor {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>(10)

    suspend fun generateData() {
        for (i in 1..5) {
            channel1.send(i)
        }
        channel1.close()
    }

    suspend fun processData() {
        for (x in channel1) {
            channel2.send(x * 2)
        }
        channel2.close()
    }

    suspend fun finalizeData() {
        for (x in channel2) {
            println("Final Data: $x")
        }
    }
}

fun mainFunction() = runBlocking {
    val processor = DataProcessor()

    launch { processor.generateData() }
    launch { processor.processData() }
    launch { processor.finalizeData() }

    additionalFunction1(this, processor)
    additionalFunction2(this, processor)
    additionalFunction3(this, processor)
}

fun additionalFunction1(scope: CoroutineScope, processor: DataProcessor) {
    scope.launch {
        for (i in 6..10) {
            processor.channel1.send(i)
        }
        processor.channel1.close()
    }
}

fun additionalFunction2(scope: CoroutineScope, processor: DataProcessor) {
    scope.launch {
        for (x in processor.channel1) {
            processor.channel2.send(x + 1)
        }
        processor.channel2.close()
    }
}

fun additionalFunction3(scope: CoroutineScope, processor: DataProcessor) {
    scope.launch {
        for (x in processor.channel2) {
            println("Additional Finalized Data: $x")
        }
    }
}

mainFunction()

class RunChecker444: RunCheckerBase() {
    override fun block() = main()
}