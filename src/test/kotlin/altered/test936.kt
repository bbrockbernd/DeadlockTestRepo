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
import org.example.altered.test936.RunChecker936.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class ClassA(val channel1: Channel<Int>, val channel2: Channel<Int>)

class ClassB(val channel3: Channel<Int>)

fun function1(channel2: Channel<Int>, channel3: Channel<Int>) {
    runBlocking(pool) {
        launch(pool) {
            channel2.send(1)
            val received = channel3.receive()
            println(received)
        }
    }
}

fun function2(channel1: Channel<Int>, channel2: Channel<Int>) {
    runBlocking(pool) {
        launch(pool) {
            val received = channel1.receive()
            println(received)
            channel2.send(received + 1)
        }
    }
}

fun function3(channel1: Channel<Int>) {
    runBlocking(pool) {
        launch(pool) {
            channel1.send(1)
            println("Sent 1 on channel1")
        }
    }
}

fun function4(classB: ClassB) {
    runBlocking(pool) {
        launch(pool) {
            val received = classB.channel3.receive()
            println(received)
        }
    }
}

fun function5(classA: ClassA, classB: ClassB) {
    runBlocking(pool) {
        coroutineScope {
            launch(pool) {
                classA.channel1.send(1)
                println("Sent 1 on channel1 in function5")
            }
            launch(pool) {
                function2(classA.channel1, classA.channel2)
            }
            launch(pool) {
                function1(classA.channel2, classB.channel3)
            }
            launch(pool) {
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
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}