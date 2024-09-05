/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":5,"nChannels":1,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 1 different channels
- 5 different coroutines
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
package org.example.generated.test217
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA(private val channel: Channel<Int>) {
    suspend fun sendData(data: Int) {
        channel.send(data)
    }
}

class ClassB(private val channel: Channel<Int>) {
    suspend fun receiveData(): Int {
        return channel.receive()
    }
}

class ClassC(private val classA: ClassA, private val classB: ClassB) {
    suspend fun process() {
        coroutineScope {
            repeat(2) {
                launch {
                    val receivedData = classB.receiveData()
                    classA.sendData(receivedData * 2)
                }
            }
        }
    }
}

fun main(): Unit= runBlocking {
    val channel = Channel<Int>()
    val classA = ClassA(channel)
    val classB = ClassB(channel)
    val classC = ClassC(classA, classB)

    launch {
        classA.sendData(10)
    }

    launch {
        classA.sendData(20)
    }

    launch {
        val data = classB.receiveData()
        println("Received data: $data")
        classA.sendData(data + 5)
    }

    launch {
        classC.process()
    }

    launch {
        val finalData = classB.receiveData()
        println("Final data: $finalData")
    }
}

