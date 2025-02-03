/* 
{"deadlock":true,"nFunctions":8,"nCoroutines":7,"nChannels":7,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
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
package org.example.altered.test232
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA(val channel1: Channel<Int>, val channel2: Channel<Int>)
class ClassB(val channel3: Channel<Int>, val channel4: Channel<Int>)
class ClassC(val channel5: Channel<Int>, val channel6: Channel<Int>, val channel7: Channel<Int>)

fun function1(channel: Channel<Int>, value: Int) {
    runBlocking {
        channel.send(value)
    }
}

suspend fun function2(channel1: Channel<Int>, channel2: Channel<Int>) {
    channel1.send(channel2.receive())
}

fun function3(channel1: Channel<Int>, channel2: Channel<Int>) {
    runBlocking {
        launch {
            val value = channel1.receive()
            channel2.send(value)
        }
    }
}

suspend fun function4(channel: Channel<Int>, classA: ClassA) {
    classA.channel1.send(channel.receive())
}

fun function5(classB: ClassB, value: Int) {
    runBlocking {
        classB.channel4.send(value)
    }
}

suspend fun function6(classC: ClassC) {
    classC.channel5.send(classC.channel6.receive())
}

fun function7(channel: Channel<Int>, value: Int) {
    runBlocking {
        launch {
            channel.send(value)
        }
    }
}

suspend fun function8(classC: ClassC, value: Int) {
    classC.channel7.send(value)
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()
    val channel6 = Channel<Int>()
    val channel7 = Channel<Int>()

    val classA = ClassA(channel1, channel2)
    val classB = ClassB(channel3, channel4)
    val classC = ClassC(channel5, channel6, channel7)

    launch { function1(channel1, 42) }
    launch { function2(channel1, channel2) }
    launch { function3(channel3, channel4) }
    launch { function4(channel5, classA) }
    launch { function5(classB, 10) }
    launch { function6(classC) }
    launch { function7(channel7, 99) }

    function8(classC, 5)
}

class RunChecker232: RunCheckerBase() {
    override fun block() = main()
}