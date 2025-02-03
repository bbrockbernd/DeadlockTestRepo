/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":5,"nChannels":1,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 1 different channels
- 5 different coroutines
- 0 different classes

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
package org.example.altered.test779
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

fun function1(channel: Channel<Int>) = runBlocking {
    launch {
        for (x in 1..5) {
            channel.send(x * x)
        }
        channel.close()
    }
}

suspend fun function2(channel: Channel<Int>) {
    coroutineScope {
        launch {
            for (y in channel) {
                function3(y)
            }
        }
    }
}

suspend fun function3(value: Int) {
    coroutineScope {
        launch {
            function4(value)
        }
    }
}

suspend fun function4(value: Int) {
    coroutineScope {
        launch {
            println("Value: $value")
        }
    }
}

fun main(): Unit= runBlocking {
    val channel = Channel<Int>()
    function1(channel)
    function2(channel)
}

class RunChecker779: RunCheckerBase() {
    override fun block() = main()
}