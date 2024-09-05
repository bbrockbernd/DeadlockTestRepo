/* 
{"deadlock":false,"nFunctions":7,"nCoroutines":2,"nChannels":5,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 5 different channels
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
package org.example.generated.test473
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Task1(val channel1: Channel<Int>) {
    suspend fun sendToChannel() {
        channel1.send(1)
    }
}

class Task2(val channel2: Channel<Int>) {
    suspend fun receiveFromChannel(): Int {
        return channel2.receive()
    }
}

class Task3(val channel1: Channel<Int>, val channel2: Channel<Int>) {
    suspend fun transferData() {
        val data = channel1.receive()
        channel2.send(data)
    }
}

class Task4(val channel3: Channel<Int>) {
    fun initData() {
        runBlocking {
            launch {
                channel3.send(3)
            }
        }
    }
}

class Task5(val channel4: Channel<Int>, val channel5: Channel<Int>) {
    fun process() {
        runBlocking {
            launch {
                val data1 = channel4.receive()
                channel5.send(data1 * 2)
            }
        }
    }
}

fun mainTask() {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()

    val task1 = Task1(channel1)
    val task2 = Task2(channel2)
    val task3 = Task3(channel1, channel2)
    val task4 = Task4(channel3)
    val task5 = Task5(channel4, channel5)

    runBlocking {
        launch {
            task4.initData()
            task1.sendToChannel()
            task3.transferData()
        }

        launch {
            val result = task2.receiveFromChannel()
            println("Result: $result")
            task5.process()
        }
    }
}

mainTask()