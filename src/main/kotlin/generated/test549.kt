/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":2,"nChannels":4,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 4 different channels
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
package org.example.generated.test549
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MyClass(val ch1: Channel<Int>, val ch2: Channel<String>) {
    suspend fun functionA() {
        ch1.send(1)
    }

    suspend fun functionB() {
        ch2.send("Hello")
    }
}

fun functionC(ch3: Channel<Int>) = runBlocking {
    coroutineScope {
        launch {
            ch3.send(2)
        }
    }
}

fun functionD(ch4: Channel<String>) = runBlocking {
    coroutineScope {
        launch {
            ch4.send("World")
        }
    }
}

fun main(): Unit= runBlocking {
    val myClass = MyClass(Channel(), Channel())
    val ch3 = Channel<Int>()
    val ch4 = Channel<String>()

    coroutineScope {
        launch {
            myClass.functionA()
            myClass.functionB()
            functionC(ch3)
            functionD(ch4)
        }

        launch {
            val received1 = myClass.ch1.receive()
            val received2 = myClass.ch2.receive()
            val received3 = ch3.receive()
            val received4 = ch4.receive()
            println("$received1, $received2, $received3, $received4")
        }
    }
}