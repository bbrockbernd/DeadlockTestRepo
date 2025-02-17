/* 
{"deadlock":false,"nFunctions":8,"nCoroutines":5,"nChannels":1,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
- 1 different channels
- 5 different coroutines
- 1 different classes

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
package org.example.altered.test67
import org.example.altered.test67.RunChecker67.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.*
import kotlinx.coroutines.delay

class ExampleClass {
    val channel = Channel<Int>()

    suspend fun produceNumbers() {
        for (i in 1..5) {
            channel.send(i)
            delay(100)
        }
        channel.close()
    }

    suspend fun consumeNumbers() {
        for (i in channel) {
            println("Received: $i")
            processNumber(i)
        }
    }

    fun processNumber(number: Int) {
        println("Processed: $number")
    }

    suspend fun startCoroutines() = coroutineScope {
        launch(pool) { produceNumbers() }
        launch(pool) { consumeNumbers() }
    }

    fun runExample() = runBlocking(pool) {
        startCoroutines()
    }
}

fun mainFunction() {
    val example = ExampleClass()
    example.runExample()
}

fun additionalFunction1() {
    println("Additional function 1")
}

fun additionalFunction2() {
    println("Additional function 2")
}

fun additionalFunction3() {
    println("Additional function 3")
}

fun additionalFunction4() {
    println("Additional function 4")
}

fun additionalFunction5() {
    println("Additional function 5")
}

fun main(): Unit{
    mainFunction()
    additionalFunction1()
    additionalFunction2()
    additionalFunction3()
    additionalFunction4()
    additionalFunction5()
}

class RunChecker67: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}