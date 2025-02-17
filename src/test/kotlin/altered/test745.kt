/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":1,"nChannels":3,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 3 different channels
- 1 different coroutines
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
package org.example.altered.test745
import org.example.altered.test745.RunChecker745.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class SharedResource {
    suspend fun process(data: Int): Int {
        delay(100)
        return data * 2
    }
}

fun createChannels(): Triple<Channel<Int>, Channel<Int>, Channel<Int>> {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    return Triple(channel1, channel2, channel3)
}

suspend fun firstFunction(ch: Channel<Int>) {
    repeat(5) {
        delay(150)
        ch.send(it)
    }
    ch.close()
}

suspend fun secondFunction(inCh: Channel<Int>, outCh: Channel<Int>, resource: SharedResource) {
    for (elem in inCh) {
        val result = resource.process(elem)
        outCh.send(result)
    }
    outCh.close()
}

suspend fun thirdFunction(ch: Channel<Int>) {
    for (elem in ch) {
        println("Received: $elem")
    }
}

fun runNonDeadlockingTest() = runBlocking(pool) {
    val (ch1, ch2, ch3) = createChannels()
    val resource = SharedResource()

    launch(pool) {
        firstFunction(ch1)
    }
    launch(pool) {
        secondFunction(ch1, ch2, resource)
    }
    launch(pool) {
        thirdFunction(ch2)
    }
}

fun main(): Unit{
    runNonDeadlockingTest()
}

class RunChecker745: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}