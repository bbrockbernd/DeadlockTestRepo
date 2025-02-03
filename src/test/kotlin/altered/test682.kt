/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":2,"nChannels":1,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 1 different channels
- 2 different coroutines
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
package org.example.altered.test682
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class TaskHandler(val channel: Channel<Int>) {
    suspend fun sendData(value: Int) = channel.send(value)
    suspend fun receiveData(): Int = channel.receive()
}

fun taskOne(handler: TaskHandler) = runBlocking {
    withContext(Dispatchers.Default) {
        handler.sendData(1)
        handleTask(handler)
    }
}

fun taskTwo(handler: TaskHandler) = runBlocking {
    withContext(Dispatchers.Default) {
        handler.sendData(2)
        handleTask(handler)
    }
}

suspend fun handleTask(handler: TaskHandler) {
    val receivedValue = handler.receiveData()
    println("Handled value: $receivedValue")
}

fun startFirstCoroutine(handler: TaskHandler) = runBlocking {
    launch {
        taskOne(handler)
    }
}

fun startSecondCoroutine(handler: TaskHandler) = runBlocking {
    launch {
        taskTwo(handler)
    }
}

fun main(): Unit{
    val channel = Channel<Int>()
    val handler = TaskHandler(channel)

    startFirstCoroutine(handler)
    startSecondCoroutine(handler)
}

class RunChecker682: RunCheckerBase() {
    override fun block() = main()
}