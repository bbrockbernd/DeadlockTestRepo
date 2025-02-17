/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":8,"nChannels":5,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 5 different channels
- 8 different coroutines
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
package org.example.altered.test340
import org.example.altered.test340.RunChecker340.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Resource(val dataChannel: Channel<String>, val ackChannel: Channel<Unit>)

fun function1(resource: Resource) {
    runBlocking(pool) {
        launch(pool) {
            resource.dataChannel.send("Message1")
            resource.ackChannel.receive()
        }
        launch(pool) {
            resource.dataChannel.send("Message2")
            resource.ackChannel.receive()
        }
    }
}

fun function2(resource: Resource) {
    runBlocking(pool) {
        launch(pool) {
            resource.dataChannel.send("Message3")
            resource.ackChannel.receive()
        }
        launch(pool) {
            resource.dataChannel.send("Message4")
            resource.ackChannel.receive()
        }
    }
}

fun function3(resource: Resource) {
    runBlocking(pool) {
        launch(pool) {
            val msg = resource.dataChannel.receive()
            println("Received: $msg")
            resource.ackChannel.send(Unit)
        }
        launch(pool) {
            val msg = resource.dataChannel.receive()
            println("Received: $msg")
            resource.ackChannel.send(Unit)
        }
        launch(pool) {
            val msg = resource.dataChannel.receive()
            println("Received: $msg")
            resource.ackChannel.send(Unit)
        }
        launch(pool) {
            val msg = resource.dataChannel.receive()
            println("Received: $msg")
            resource.ackChannel.send(Unit)
        }
    }
}

fun main(): Unit {
    val dataChannel = Channel<String>()
    val ackChannel = Channel<Unit>()
    val resource = Resource(dataChannel, ackChannel)

    runBlocking(pool) {
        launch(pool) {
            function1(resource)
        }
        launch(pool) {
            function2(resource)
        }
        launch(pool) {
            function3(resource)
        }
    }
}

class RunChecker340: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}