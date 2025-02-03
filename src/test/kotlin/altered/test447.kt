/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":4,"nChannels":1,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 1 different channels
- 4 different coroutines
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
package org.example.altered.test447
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Processor {
    val channel = Channel<Int>()

    fun process() = runBlocking {
        coroutineScope {
            launch { firstAction() }
            launch { secondAction() }
            launch { thirdAction() }
            launch { fourthAction() }
        }
    }

    private suspend fun firstAction() {
        channel.send(1)
        println("First action completed")
    }

    private suspend fun secondAction() {
        val x = channel.receive()
        channel.send(x + 1)
        println("Second action completed")
    }

    private suspend fun thirdAction() {
        val y = channel.receive()
        channel.send(y + 2)
        println("Third action completed")
    }

    private suspend fun fourthAction() {
        val z = channel.receive()
        println("Fourth action completed with $z")
    }
}

class Manager {
    fun start() {
        val processor = Processor()
        processor.process()
    }
}

class Application {
    fun run() {
        Manager().start()
    }
}

fun main(): Unit{
    Application().run()
}

class RunChecker447: RunCheckerBase() {
    override fun block() = main()
}