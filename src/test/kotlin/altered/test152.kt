/* 
{"deadlock":true,"nFunctions":7,"nCoroutines":8,"nChannels":1,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 1 different channels
- 8 different coroutines
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
package org.example.altered.test152
import org.example.altered.test152.RunChecker152.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlin.coroutines.CoroutineContext

class ClassOne(val channel: Channel<Int>)
class ClassTwo(val channel: Channel<Int>)
class ClassThree(val channel: Channel<Int>)
class ClassFour(val channel: Channel<Int>)
class ClassFive(val channel: Channel<Int>)

fun funcOne(channel: Channel<Int>) {
    runBlocking(pool) {
        channel.send(1)
        channel.receive()
    }
}

fun funcTwo(channel: Channel<Int>) {
    runBlocking(pool) {
        channel.send(2)
    }
}

fun funcThree(channel: Channel<Int>) {
    runBlocking(pool) {
        channel.receive()
    }
}

fun funcFour(channel: Channel<Int>) {
    runBlocking(pool) {
        coroutineScope {
            launch(pool) {
                channel.receive()
            }
            launch(pool) {
                channel.send(4)
            }
        }
    }
}

fun funcFive(channel: Channel<Int>) {
    runBlocking(pool) {
        coroutineScope {
            launch(pool) {
                channel.send(5)
            }
            launch(pool) {
                channel.receive()
            }
        }
    }
}

fun funcSix(channel: Channel<Int>) = runBlocking(pool) {
    coroutineScope {
        launch(pool) {
            channel.send(6)
            channel.receive()
        }
    }
}

fun funcSeven(channel: Channel<Int>) = runBlocking(pool) {
    coroutineScope {
        launch(pool) {
            channel.send(7)
        }
        launch(pool) {
            channel.receive()
        }
    }
}

fun main(): Unit{
    val channel = Channel<Int>()
    val classOne = ClassOne(channel)
    val classTwo = ClassTwo(channel)
    val classThree = ClassThree(channel)
    val classFour = ClassFour(channel)
    val classFive = ClassFive(channel)

    runBlocking(pool) {
        launch(pool) { funcOne(classOne.channel) }
        launch(pool) { funcTwo(classTwo.channel) }
        launch(pool) { funcThree(classThree.channel) }
        launch(pool) { funcFour(classFour.channel) }
        launch(pool) { funcFive(classFive.channel) }
        launch(pool) { funcSix(classOne.channel) }
        launch(pool) { funcSeven(classThree.channel) }
        launch(pool) { funcOne(classFive.channel) }
    }
}

class RunChecker152: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}