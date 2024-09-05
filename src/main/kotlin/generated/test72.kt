/* 
{"deadlock":true,"nFunctions":2,"nCoroutines":4,"nChannels":7,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 2 different functions
- 7 different channels
- 4 different coroutines
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
package org.example.generated.test72
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA(val channel1: Channel<Int>, val channel2: Channel<Int>)
class ClassB(val channel3: Channel<Int>, val channel4: Channel<Int>)
class ClassC(val channel5: Channel<Int>)
class ClassD(val channel6: Channel<Int>)
class ClassE(val channel7: Channel<Int>)

fun function1(a: ClassA, b: ClassB) {
    runBlocking {
        launch {
            a.channel1.send(1) // sends 1
            val received = a.channel2.receive() // waits for receive
            println("Function1, Coroutine1, received: $received")
        }
        launch {
            b.channel3.send(2) // sends 2
            val received = b.channel4.receive() // waits for receive
            println("Function1, Coroutine2, received: $received")
        }
    }
}

fun function2(c: ClassC, d: ClassD, e: ClassE) {
    runBlocking {
        launch {
            val received = c.channel5.receive() // waits for receive
            d.channel6.send(received + 1) // sends received + 1
        }
        launch {
            val received = e.channel7.receive() // waits for receive
            d.channel6.send(received + 2) // sends received + 2
        }
    }
}

fun main(): Unit{
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()
    val channel6 = Channel<Int>()
    val channel7 = Channel<Int>()

    val a = ClassA(channel1, channel2)
    val b = ClassB(channel3, channel4)
    val c = ClassC(channel5)
    val d = ClassD(channel6)
    val e = ClassE(channel7)

    runBlocking {
        launch {
            function1(a, b)
        }
        launch {
            function2(c, d, e)
        }
    }
}