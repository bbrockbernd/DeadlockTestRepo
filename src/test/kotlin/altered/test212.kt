/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":6,"nChannels":2,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 2 different channels
- 6 different coroutines
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
package org.example.altered.test212
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Data1(val value: Int)
class Data2(val value: Int)
class Data3(val value: Int)
class Data4(val value: String)
class Data5(val value: String)

fun producer(channel1: Channel<Data1>, channel2: Channel<Data2>) = runBlocking {
    repeat(3) {
        launch {
            val data = Data1(it)
            channel1.send(data)
        }
    }
    
    repeat(2) {
        launch {
            val data = Data2(it)
            channel2.send(data)
        }
    }
}

fun consumer(channel1: Channel<Data1>, channel2: Channel<Data2>) = runBlocking {
    repeat(2) {
        launch {
            val data = channel1.receive()
            println("Data1 received: ${data.value}")
        }
    }
    
    repeat(2) {
        launch {
            val data = channel2.receive()
            println("Data2 received: ${data.value}")
        }
    }
}

fun main(): Unit = runBlocking {
    val channel1 = Channel<Data1>()
    val channel2 = Channel<Data2>()

    launch {
        producer(channel1, channel2)
    }

    launch {
        consumer(channel1, channel2)
    }

    coroutineScope {
        launch {
            delay(1000)
            val data = Data3(3)
            println("Data3 produced: ${data.value}")
        }
        
        launch {
            delay(1000)
            val data = Data4("Hello")
            println("Data4 produced: ${data.value}")
        }
        
        launch {
            delay(1000)
            val data = Data5("World")
            println("Data5 produced: ${data.value}")
        }
    }
}

class RunChecker212: RunCheckerBase() {
    override fun block() = main()
}