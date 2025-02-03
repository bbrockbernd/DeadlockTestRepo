/* 
{"deadlock":true,"nFunctions":1,"nCoroutines":5,"nChannels":4,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
- 4 different channels
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
package org.example.altered.test918
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA(val channel: Channel<Int>)
class ClassB(val channel: Channel<Int>)
class ClassC(val channel: Channel<Int>)

fun someFunction(classA: ClassA, classB: ClassB, classC: ClassC, ch4: Channel<Int>) = runBlocking {
    val channel1 = classA.channel
    val channel2 = classB.channel
    val channel3 = classC.channel

    launch {
        channel1.send(1)
        channel2.receive()
    }

    launch {
        channel2.send(2)
        channel3.receive()
    }

    launch {
        channel3.send(3)
        ch4.receive()
    }

    launch {
        ch4.send(4)
        channel1.receive()
    }
    
    launch {
        delay(1000)
        println("This coroutine is not involved in the deadlock.")
    }
}

val ch1 = Channel<Int>()
val ch2 = Channel<Int>()
val ch3 = Channel<Int>()
val ch4 = Channel<Int>()

val classA = ClassA(ch1)
val classB = ClassB(ch2)
val classC = ClassC(ch3)

someFunction(classA, classB, classC, ch4)

class RunChecker918: RunCheckerBase() {
    override fun block() = main()
}