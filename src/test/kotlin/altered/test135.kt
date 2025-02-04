/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":2,"nChannels":7,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 7 different channels
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
package org.example.altered.test135
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class ClassA(val channel: Channel<Int>)
class ClassB(val channel: Channel<Int>)
class ClassC(val channel: Channel<Int>)
class ClassD(val channel: Channel<Int>)
class ClassE(val channel: Channel<Int>)

fun function1(channel1: Channel<Int>) {
    GlobalScope.launch {
        repeat(5) {
            channel1.send(it)
        }
        channel1.close()
    }
}

suspend fun function2(channel2: Channel<Int>, channel3: Channel<Int>) {
    coroutineScope {
        launch {
            for (i in channel2) {
                channel3.send(i * 2)
            }
            channel3.close()
        }
    }
}

fun function3(channel4: Channel<Int>) {
    GlobalScope.launch {
        for (i in 1..5) {
            channel4.send(i * 3)
        }
        channel4.close()
    }
}

suspend fun function4(channel5: Channel<Int>, channel6: Channel<Int>) {
    coroutineScope {
        launch {
            for (i in channel5) {
                channel6.send(i + 4)
            }
            channel6.close()
        }
    }
}

fun function5(channel7: Channel<Int>) {
    GlobalScope.launch {
        for (i in 1..5) {
            channel7.send(i * 5)
        }
        channel7.close()
    }
}

fun main(): Unit = runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()
    val channel6 = Channel<Int>()
    val channel7 = Channel<Int>()

    val classA = ClassA(channel1)
    val classB = ClassB(channel2)
    val classC = ClassC(channel3)
    val classD = ClassD(channel4)
    val classE = ClassE(channel5)

    function1(classA.channel)
    function3(classD.channel)
    function5(classE.channel)

    launch {
        function2(classB.channel, classC.channel)
    }

    launch {
        function4(classC.channel, classD.channel)
    }

    launch {
        for (i in classD.channel) {
            println("Received from channel4: $i")
        }
    }

    launch {
        for (i in channel7) {
            println("Received from channel7: $i")
        }
    }
}

class RunChecker135: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}