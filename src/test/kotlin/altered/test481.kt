/* 
{"deadlock":false,"nFunctions":7,"nCoroutines":3,"nChannels":7,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 7 different channels
- 3 different coroutines
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
package org.example.altered.test481
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA(val channel: Channel<String>)
class ClassB(val channel: Channel<String>)
class ClassC(val channel: Channel<String>)
class ClassD(val channel: Channel<String>)
class ClassE(val channel: Channel<String>)

fun function1(channel: Channel<String>) {
    runBlocking {
        channel.send("Message from function1")
    }
}

fun function2(channel: Channel<String>) {
    runBlocking {
        channel.send("Message from function2")
    }
}

fun function3(channel: Channel<String>) {
    runBlocking {
        println(channel.receive())
    }
}

suspend fun function4(channel1: Channel<String>, channel2: Channel<String>) {
    channel1.send("Message from function4")
    println(channel2.receive())
}

suspend fun function5(channel: Channel<String>) {
    channel.send("Message from function5")
}

suspend fun function6(channel1: Channel<String>, channel2: Channel<String>) {
    coroutineScope {
        launch {
            println(channel1.receive())
            channel2.send("Message from function6")
        }
    }
}

fun function7(channel: Channel<String>) {
    runBlocking {
        println(channel.receive())
        channel.send("Reply from function7")
    }
}

fun main(): Unit= runBlocking {
    val channel1 = Channel<String>()
    val channel2 = Channel<String>()
    val channel3 = Channel<String>()
    val channel4 = Channel<String>()
    val channel5 = Channel<String>()
    val channel6 = Channel<String>()
    val channel7 = Channel<String>()

    val classA = ClassA(channel1)
    val classB = ClassB(channel2)
    val classC = ClassC(channel3)
    val classD = ClassD(channel4)
    val classE = ClassE(channel5)

    launch { function1(classA.channel) }
    launch { function2(classB.channel) }
    launch { function3(classC.channel) }
    launch { function4(classD.channel, classE.channel) }
    launch { function5(channel6) }
    launch { function6(channel6, channel7) }
    function7(channel7)
}

class RunChecker481: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}