/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":2,"nChannels":1,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 1 different channels
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
package org.example.generated.test435
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA {
    suspend fun functionA(channel: Channel<String>) {
        // Function receiving from the channel
        repeat(5) {
            val message = channel.receive()
            println("ClassA received: $message")
        }
    }
}

class ClassB {
    suspend fun functionB(channel: Channel<String>) {
        // Function sending to the channel
        repeat(5) {
            channel.send("Message from ClassB $it")
        }
    }
}

class ClassC {
    suspend fun functionC() {
        // A dummy function for additional logic
        println("Performing some logic in ClassC")
    }
}

class ClassD {
    suspend fun functionD() {
        // Another dummy function for additional logic
        println("Performing additional logic in ClassD")
    }
}

class ClassE {
    fun createChannel(): Channel<String> {
        return Channel()
    }
}

fun main(): Unit= runBlocking {
    val channel = ClassE().createChannel()

    val job1 = launch {
        ClassA().functionA(channel)
    }

    val job2 = launch {
        ClassB().functionB(channel)
    }

    ClassC().functionC()
    ClassD().functionD()

    job1.join()
    job2.join()
}