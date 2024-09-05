/* 
{"deadlock":false,"nFunctions":2,"nCoroutines":2,"nChannels":3,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 3 different channels
- 2 different coroutines
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
package org.example.generated.test128
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Data1(val value: Int)
class Data2(val value: String)
class Data3(val value: Boolean)

class Processor1(private val ch1: Channel<Data1>, private val ch2: Channel<Data2>) {
    suspend fun process() {
        for (i in 1..5) {
            ch1.send(Data1(i))
            val received = ch2.receive()
            println("Processor1 received Data2: ${received.value}")
        }
    }
}

class Processor2(private val ch2: Channel<Data2>, private val ch3: Channel<Data3>) {
    suspend fun process() {
        for (i in 1..5) {
            ch2.send(Data2("data$i"))
            val received = ch3.receive()
            println("Processor2 received Data3: ${received.value}")
        }
    }
}

class Orchestrator(private val ch1: Channel<Data1>, private val ch3: Channel<Data3>) {
    suspend fun orchestrate() {
        for (i in 1..5) {
            val received = ch1.receive()
            println("Orchestrator received Data1: ${received.value}")
            ch3.send(Data3(received.value % 2 == 0))
        }
    }
}

fun func1(ch1: Channel<Data1>, ch2: Channel<Data2>) {
    runBlocking {
        val processor1 = Processor1(ch1, ch2)
        launch { processor1.process() }
    }
}

fun func2(ch2: Channel<Data2>, ch3: Channel<Data3>, ch1: Channel<Data1>) {
    runBlocking {
        val processor2 = Processor2(ch2, ch3)
        val orchestrator = Orchestrator(ch1, ch3)
        launch { processor2.process() }
        launch { orchestrator.orchestrate() }
    }
}

fun main(): Unit{
    val ch1 = Channel<Data1>()
    val ch2 = Channel<Data2>()
    val ch3 = Channel<Data3>()

    func1(ch1, ch2)
    func2(ch2, ch3, ch1)
}