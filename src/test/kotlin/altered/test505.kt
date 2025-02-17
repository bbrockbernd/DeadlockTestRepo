/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":2,"nChannels":3,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 3 different channels
- 2 different coroutines
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
package org.example.altered.test505
import org.example.altered.test505.RunChecker505.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ComponentA(val channel: Channel<Int>)
class ComponentB(val channel: Channel<Int>)
class ComponentC(val channel: Channel<Int>)

fun function1(componentA: ComponentA, componentB: ComponentB) {
    runBlocking(pool) {
        launch(pool) {
            // Trying to receive but no one sends initially causing potential deadlock
            val received = componentA.channel.receive()
            componentB.channel.send(received)
        }
    }
}

suspend fun function2(componentC: ComponentC, channel: Channel<Int>) {
    coroutineScope {
        launch(pool) {
            // Waiting to send, could potentially deadlock because no one is receiving yet
            componentC.channel.send(42)
        }
        val received = channel.receive() // Blocks waiting for a value
        componentC.channel.send(received)
    }
}

fun function3(channel1: Channel<Int>, channel2: Channel<Int>) {
    runBlocking(pool) {
        launch(pool) {
            // Two coroutines depend on each other for values causing deadlock
            val received1 = channel1.receive()
            channel2.send(received1)
        }
        launch(pool) {
            val received2 = channel2.receive()
            channel1.send(received2)
        }
    }
}

fun function4() {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>(1) // Buffered channel to allow one send without immediate receive

    val componentA = ComponentA(channel1)
    val componentB = ComponentB(channel2)
    val componentC = ComponentC(channel3)

    function1(componentA, componentB)

    runBlocking(pool) {
        launch(pool) {
            // Potential deadlock if function2 channel receive waits for send which is also waiting
            function2(componentC, channel3)
        }
    }

    function3(channel1, channel2)
}

fun main(): Unit{
    function4() // Entry point to start the test
}

class RunChecker505: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}