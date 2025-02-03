/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":7,"nChannels":7,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 7 different channels
- 7 different coroutines
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
package org.example.altered.test341
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA(val chan1: Channel<Int>, val chan2: Channel<Int>)
class ClassB(val chan3: Channel<Int>, val chan4: Channel<Int>)
class ClassC(val chan5: Channel<Int>, val chan6: Channel<Int>, val chan7: Channel<Int>)

fun function1(classA: ClassA) = runBlocking {
    launch {
        repeat(5) {
            classA.chan1.send(it)
        }
    }
    launch {
        repeat(5) {
            classA.chan2.send(it * 2)
        }
    }
}

fun function2(classB: ClassB) = runBlocking {
    launch {
        repeat(5) {
            val value = classB.chan3.receive()
            println("Chan3 received in function2: $value")
        }
    }
    launch {
        repeat(5) {
            val value = classB.chan4.receive()
            println("Chan4 received in function2: $value")
        }
    }
}

fun function3(classC: ClassC) = runBlocking {
    launch {
        repeat(5) {
            classC.chan5.send(it + 10)
        }
    }
}

suspend fun function4(chan: Channel<Int>) {
    coroutineScope {
        launch {
            repeat(5) {
                val value = chan.receive()
                println("Received in function4: $value")
            }
        }
    }
}

fun function5(classC: ClassC) = runBlocking {
    launch {
        repeat(5) {
            classC.chan6.send(it + 20)
        }
    }
    launch {
        function4(classC.chan7)
    }
}

fun main(): Unit= runBlocking {
    val chan1 = Channel<Int>()
    val chan2 = Channel<Int>()
    val chan3 = Channel<Int>()
    val chan4 = Channel<Int>()
    val chan5 = Channel<Int>()
    val chan6 = Channel<Int>()
    val chan7 = Channel<Int>()

    val classA = ClassA(chan1, chan2)
    val classB = ClassB(chan3, chan4)
    val classC = ClassC(chan5, chan6, chan7)

    function1(classA)
    function2(classB)

    launch {
        repeat(5) {
            chan3.send(it + 30)
            chan4.send(it + 40)
        }
    }

    function3(classC)
    function5(classC)
}

class RunChecker341: RunCheckerBase() {
    override fun block() = main()
}