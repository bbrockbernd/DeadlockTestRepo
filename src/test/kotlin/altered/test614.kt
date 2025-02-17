/* 
{"deadlock":true,"nFunctions":1,"nCoroutines":4,"nChannels":4,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 1 different functions
- 4 different channels
- 4 different coroutines
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
package org.example.altered.test614
import org.example.altered.test614.RunChecker614.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA(val channelA: Channel<Int>)
class ClassB(val channelB: Channel<Int>)
class ClassC(val channelC: Channel<Int>)
class ClassD(val channelD: Channel<Int>)

fun main(): Unit= runBlocking(pool) {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()

    val classA = ClassA(channel1)
    val classB = ClassB(channel2)
    val classC = ClassC(channel3)
    val classD = ClassD(channel4)

    launch(pool) { coroutine1(classA, classB) }
    launch(pool) { coroutine2(classB, classC) }
    launch(pool) { coroutine3(classC, classD) }
    launch(pool) { coroutine4(classD, classA) }
}

suspend fun coroutine1(classA: ClassA, classB: ClassB) {
    val value = classA.channelA.receive()
    classB.channelB.send(value)
}

suspend fun coroutine2(classB: ClassB, classC: ClassC) {
    val value = classB.channelB.receive()
    classC.channelC.send(value)
}

suspend fun coroutine3(classC: ClassC, classD: ClassD) {
    val value = classC.channelC.receive()
    classD.channelD.send(value)
}

suspend fun coroutine4(classD: ClassD, classA: ClassA) {
    val value = classD.channelD.receive()
    classA.channelA.send(value)
}

class RunChecker614: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}