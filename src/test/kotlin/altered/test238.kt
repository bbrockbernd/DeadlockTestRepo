/* 
{"deadlock":false,"nFunctions":4,"nCoroutines":3,"nChannels":2,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 4 different functions
- 2 different channels
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
package org.example.altered.test238
import org.example.altered.test238.RunChecker238.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class ClassA(val channel: Channel<Int>) {
    suspend fun sendToChannel(value: Int) {
        channel.send(value)
    }
}

class ClassB(val channel: Channel<Int>) {
    suspend fun receiveFromChannel(): Int {
        return channel.receive()
    }
}

class ClassC(val channel1: Channel<Int>, val channel2: Channel<Int>) {
    suspend fun transfer() {
        val value = channel1.receive()
        channel2.send(value)
    }
}

class ClassD
class ClassE

suspend fun function1(classA: ClassA) {
    classA.sendToChannel(10)
}

suspend fun function2(classB: ClassB): Int {
    return classB.receiveFromChannel()
}

suspend fun function3(classC: ClassC) {
    classC.transfer()
}

fun function4(classD: ClassD, classE: ClassE) {
    // Dummy function to fit the 4-function requirement
}

fun main(): Unit= runBlocking(pool) {
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    
    val classA = ClassA(channel1)
    val classB = ClassB(channel2)
    val classC = ClassC(channel1, channel2)
    val classD = ClassD()
    val classE = ClassE()

    launch(pool) {
        function1(classA)
    }

    launch(pool) {
        function3(classC)
    }

    launch(pool) {
        val receivedValue = function2(classB)
        println("Received value: $receivedValue")
    }
    
    // Calling dummy function4 to fit the requirements
    function4(classD, classE)
}

class RunChecker238: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}