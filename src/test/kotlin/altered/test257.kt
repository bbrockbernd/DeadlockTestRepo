/* 
{"deadlock":true,"nFunctions":6,"nCoroutines":2,"nChannels":5,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 6 different functions
- 5 different channels
- 2 different coroutines
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
package org.example.altered.test257
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class ClassA(val ch1: Channel<Int>, val ch2: Channel<Int>) {
    suspend fun sendToCh1(value: Int) {
        ch1.send(value)
    }

    suspend fun receiveFromCh2(): Int {
        return ch2.receive()
    }
}

class ClassB(val ch3: Channel<Int>, val ch4: Channel<Int>) {
    suspend fun sendToCh3(value: Int) {
        ch3.send(value)
    }

    suspend fun receiveFromCh4(): Int {
        return ch4.receive()
    }
}

class ClassC(val ch5: Channel<Int>) {
    suspend fun sendToCh5(value: Int) {
        ch5.send(value)
    }

    suspend fun receiveFromCh5(): Int {
        return ch5.receive()
    }
}

suspend fun function1(classA: ClassA, value: Int) {
    classA.sendToCh1(value)
}

suspend fun function2(classB: ClassB, value: Int) {
    classB.sendToCh3(value)
}

suspend fun function3(classC: ClassC, value: Int) {
    classC.sendToCh5(value)
}

fun main(): Unit= runBlocking {
    val ch1 = Channel<Int>()
    val ch2 = Channel<Int>()
    val ch3 = Channel<Int>()
    val ch4 = Channel<Int>()
    val ch5 = Channel<Int>()

    val classA = ClassA(ch1, ch2)
    val classB = ClassB(ch3, ch4)
    val classC = ClassC(ch5)

    launch {
        function1(classA, 1)
        classB.sendToCh3(classA.receiveFromCh2())
    }

    launch {
        function2(classB, 2)
        classC.sendToCh5(classB.receiveFromCh4())
    }

    function3(classC, 3)
    ch2.send(ch1.receive())
    ch4.send(ch3.receive())
}

class RunChecker257: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}