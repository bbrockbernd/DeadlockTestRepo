/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":5,"nChannels":3,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 3 different channels
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
package org.example.generated.test631
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class ClassA(val ch1: Channel<Int>, val ch2: Channel<Int>, val ch3: Channel<Int>) {
    suspend fun functionA() {
        ch1.send(1)
        ch2.receive()
    }
}

class ClassB(val ch1: Channel<Int>, val ch2: Channel<Int>, val ch3: Channel<Int>) {
    suspend fun functionB() {
        ch2.send(2)
        ch3.receive()
    }
}

class ClassC(val ch1: Channel<Int>, val ch2: Channel<Int>, val ch3: Channel<Int>) {
    suspend fun functionC() {
        ch3.send(3)
        ch1.receive()
    }
}

suspend fun functionD(ch1: Channel<Int>, ch2: Channel<Int>, ch3: Channel<Int>) {
    ch1.send(4)
    ch2.receive()
}

suspend fun functionE(ch1: Channel<Int>, ch2: Channel<Int>, ch3: Channel<Int>) {
    ch2.send(5)
    ch3.receive()
}

fun main(): Unit= runBlocking {
    val ch1 = Channel<Int>()
    val ch2 = Channel<Int>()
    val ch3 = Channel<Int>()
    
    val classA = ClassA(ch1, ch2, ch3)
    val classB = ClassB(ch1, ch2, ch3)
    val classC = ClassC(ch1, ch2, ch3)

    launch { classA.functionA() }
    launch { classB.functionB() }
    launch { classC.functionC() }
    launch { functionD(ch1, ch2, ch3) }
    launch { functionE(ch1, ch2, ch3) }
}