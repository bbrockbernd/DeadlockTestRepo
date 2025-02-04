/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":3,"nChannels":2,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 2 different channels
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
package org.example.altered.test923
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Worker {
    suspend fun process(ch1: Channel<Int>, ch2: Channel<Int>) {
        for (i in 1..5) {
            val received = ch1.receive()
            ch2.send(received * 2)
        }
    }
}

suspend fun sender(ch: Channel<Int>) {
    repeat(5) {
        ch.send(it + 1)
    }
}

suspend fun receiver(ch: Channel<Int>) {
    repeat(5) {
        println(ch.receive())
    }
}

fun main(): Unit= runBlocking {
    val ch1 = Channel<Int>()
    val ch2 = Channel<Int>()
    val worker = Worker()

    launch { sender(ch1) }
    launch { worker.process(ch1, ch2) }
    launch { receiver(ch2) }
}

class RunChecker923: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}