/* 
{"deadlock":true,"nFunctions":4,"nCoroutines":7,"nChannels":1,"nClasses":0}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 1 different channels
- 7 different coroutines
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
package org.example.generated.test456
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun main(): Unit= runBlocking {
    val channel = Channel<Int>()

    launch { 
        coroutineFunction1(channel)
    }
    launch { 
        coroutineFunction2(channel)
    }
    launch { 
        coroutineFunction3(channel)
    }
    launch { 
        coroutineFunction4(channel)
    }
    launch { 
        coroutineFunction1(channel)
    }
    launch { 
        coroutineFunction2(channel)
    }
    launch { 
        coroutineFunction3(channel)
    }
    launch { 
        coroutineFunction4(channel)
    }
}

suspend fun coroutineFunction1(channel: Channel<Int>) {
    coroutineScope {
        launch {
            channel.send(1)
        }

        launch {
            channel.receive()
        }
    }
}

suspend fun coroutineFunction2(channel: Channel<Int>) {
    coroutineScope {
        launch {
            channel.send(2)
        }

        launch {
            channel.receive()
        }
    }
}

suspend fun coroutineFunction3(channel: Channel<Int>) {
    coroutineScope {
        launch {
            channel.send(3)
        }

        launch {
            channel.receive()
        }
    }
}

suspend fun coroutineFunction4(channel: Channel<Int>) {
    coroutineScope {
        launch {
            channel.send(4)
        }

        launch {
            channel.receive()
        }
    }
}