/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":2,"nChannels":2,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 2 different channels
- 2 different coroutines
- 2 different classes

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
package org.example.generated.test792
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class FirstClass(private val channel: Channel<String>) {
    suspend fun sendMessage() {
        channel.send("Message from FirstClass")
    }
}

class SecondClass(private val channel: Channel<String>) {
    suspend fun receiveMessage() : String {
        return channel.receive()
    }
}

suspend fun sendToFirstClass(firstClass: FirstClass) {
    firstClass.sendMessage()
}

suspend fun receiveFromSecondClass(secondClass: SecondClass): String {
    return secondClass.receiveMessage()
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<String>()
    val channel2 = Channel<String>()

    val firstClassInstance = FirstClass(channel1)
    val secondClassInstance = SecondClass(channel2)

    launch {
        sendToFirstClass(firstClassInstance)
    }

    launch {
        val message = receiveFromSecondClass(secondClassInstance)
        println(message)
    }
    
    delay(1000L) // Wait to ensure coroutines complete
}