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
package org.example.generated.test340
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Resource(val dataChannel: Channel<String>, val ackChannel: Channel<Unit>)

fun function1(resource: Resource) {
    runBlocking {
        launch {
            resource.dataChannel.send("Message1")
            resource.ackChannel.receive()
        }
        launch {
            resource.dataChannel.send("Message2")
            resource.ackChannel.receive()
        }
    }
}

fun function2(resource: Resource) {
    runBlocking {
        launch {
            resource.dataChannel.send("Message3")
            resource.ackChannel.receive()
        }
        launch {
            resource.dataChannel.send("Message4")
            resource.ackChannel.receive()
        }
    }
}

fun function3(resource: Resource) {
    runBlocking {
        launch {
            val msg = resource.dataChannel.receive()
            println("Received: $msg")
            resource.ackChannel.send(Unit)
        }
        launch {
            val msg = resource.dataChannel.receive()
            println("Received: $msg")
            resource.ackChannel.send(Unit)
        }
        launch {
            val msg = resource.dataChannel.receive()
            println("Received: $msg")
            resource.ackChannel.send(Unit)
        }
        launch {
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

    runBlocking {
        launch {
            function1(resource)
        }
        launch {
            function2(resource)
        }
        launch {
            function3(resource)
        }
    }
}