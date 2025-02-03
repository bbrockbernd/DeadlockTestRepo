/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":4,"nChannels":3,"nClasses":2}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 3 different channels
- 4 different coroutines
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
package org.example.altered.test936
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class ClassA(val channel1: Channel<Int>, val channel2: Channel<Int>)

class ClassB(val channel3: Channel<Int>)

fun function1(channel2: Channel<Int>, channel3: Channel<Int>) {
    runBlocking {
        launch {
            channel2.send(1)
            val received = channel3.receive()
            println(received)
        }
    }
}

fun function2(channel1: Channel<Int>, channel2: Channel<Int>) {
    runBlocking {
        launch {
            val received = channel1.receive()
            println(received)
            channel2.send(received + 1)
        }
    }
}

fun function3(channel1: Channel<Int>) {
    runBlocking {
        launch {
            channel1.send(1)
            println("Sent 1 on channel1")
        }
    }
}

fun function4(classB: ClassB) {
    runBlocking {
        launch {
            val received = classB.channel3.receive()
            println(received)
        }
    }
}

fun function5(classA: ClassA, classB: ClassB) {
    runBlocking {
        coroutineScope {
            launch {
                classA.channel1.send(1)
                println("Sent 1 on channel1 in function5")
            }
            launch {
                function2(classA.channel1, classA.channel2)
            }
            launch {
                function1(classA.channel2, classB.channel3)
            }
            launch {
                function4(classB)
            }
        }
    }
}

fun main(): Unit{
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()

    val classA = ClassA(channel1, channel2)
    val classB = ClassB(channel3)

    function5(classA, classB)
}

class RunChecker936: RunCheckerBase() {
    override fun block() = main()
}