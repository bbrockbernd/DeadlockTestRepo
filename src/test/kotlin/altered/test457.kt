/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":5,"nChannels":1,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 1 different channels
- 5 different coroutines
- 2 different classes

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
package org.example.altered.test457
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Processor {
    val channel = Channel<Int>()
    suspend fun processMessage(msg: Int) {
        delay(100)
        println("Processing $msg")
    }
}

class Generator {
    suspend fun generate(channel: Channel<Int>) {
        repeat(5) {
            delay(50)
            channel.send(it)
            println("Generated $it")
        }
    }
}

fun prepareProcessor(processor: Processor) {
    runBlocking {
        launch {
            repeat(5) {
                val msg = processor.channel.receive()
                processor.processMessage(msg)
            }
        }
    }
}

fun startGenerator(generator: Generator, channel: Channel<Int>) {
    runBlocking {
        launch {
            generator.generate(channel)
        }
    }
}

fun overallTask() {
    val processor = Processor()
    val generator = Generator()

    startGenerator(generator, processor.channel)
    prepareProcessor(processor)
}

fun main(): Unit{
    overallTask()
}

class RunChecker457: RunCheckerBase() {
    override fun block() = main()
}