/* 
{"deadlock":false,"nFunctions":8,"nCoroutines":1,"nChannels":5,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
- 5 different channels
- 1 different coroutines
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
package org.example.altered.test270
import org.example.altered.test270.RunChecker270.Companion.pool
import org.example.altered.RunCheckerBase
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class MyClass(val channel1: Channel<Int>, val channel2: Channel<Int>, val channel3: Channel<Int>) {
    suspend fun function1() {
        val result = channel1.receive()
        println("Function1 received: $result")
    }

    suspend fun function2() {
        channel2.send(5)
        println("Function2 sent: 5")
    }

    fun function3(): Int {
        return 10
    }

    suspend fun function4(channel4: Channel<Int>) {
        val result = channel4.receive()
        println("Function4 received: $result")
        channel3.send(result + 1)
        println("Function4 sent: ${result + 1}")
    }
}

suspend fun function5(channel5: Channel<Int>) {
    channel5.send(20)
    println("Function5 sent: 20")
}

suspend fun function6(channel3: Channel<Int>) {
    val result = channel3.receive()
    println("Function6 received: $result")
}

suspend fun function7(channel4: Channel<Int>, channel5: Channel<Int>) {
    val result = channel5.receive()
    channel4.send(result + 1)
    println("Function7 sent: ${result + 1} to Channel4")
}

fun function8(myClass: MyClass, channel4: Channel<Int>) {
    runBlocking(pool) {
        launch(pool) {
            myClass.function1()
            myClass.function2()
            myClass.function4(channel4)
            function5(channel4)
            function6(myClass.channel3)
            function7(channel4, myClass.channel1)
        }
    }
}

fun main(): Unit{
    val channel1 = Channel<Int>()
    val channel2 = Channel<Int>()
    val channel3 = Channel<Int>()
    val channel4 = Channel<Int>()
    val channel5 = Channel<Int>()
    
    val myClass = MyClass(channel1, channel2, channel3)
    function8(myClass, channel4)
}

class RunChecker270: RunCheckerBase() {
    companion object {
        lateinit var pool: ExecutorCoroutineDispatcher
    }
    override fun block() {
        pool = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        runBlocking(pool) { main() }
    }}