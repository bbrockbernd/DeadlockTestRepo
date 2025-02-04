/* 
{"deadlock":true,"nFunctions":3,"nCoroutines":3,"nChannels":4,"nClasses":4}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 4 different channels
- 3 different coroutines
- 4 different classes

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
package org.example.altered.test74
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA(val channel1: Channel<Int>, val channel2: Channel<Int>)
class ClassB(val channel3: Channel<Int>, val channel4: Channel<Int>)
class ClassC(val channel1: Channel<Int>, val channel3: Channel<Int>)
class ClassD(val channel2: Channel<Int>, val channel4: Channel<Int>)

suspend fun function1(channel1: Channel<Int>, channel2: Channel<Int>) {
    channel1.send(1)
    channel2.send(1)
}

suspend fun function2(channel3: Channel<Int>, channel4: Channel<Int>) {
    channel3.send(2)
    channel4.send(2)
}

suspend fun function3(channel1: Channel<Int>, channel3: Channel<Int>) {
    val receivedValue1 = channel1.receive()
    val receivedValue3 = channel3.receive()
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()

    val classA = ClassA(channel1, channel2)
    val classB = ClassB(channel3, channel4)
    val classC = ClassC(channel1, channel3)
    val classD = ClassD(channel2, channel4)

    launch {
        function1(classA.channel1, classA.channel2)
    }

    launch {
        function2(classB.channel3, classB.channel4)
    }

    launch {
        function3(classC.channel1, classC.channel3)
    }
}

class RunChecker74: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}