/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":1,"nChannels":1,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 1 different channels
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
package org.example.altered.test560
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class SampleClass(private val channel: Channel<Int>) {
    suspend fun produce(num: Int) {
        channel.send(num)
    }
    
    suspend fun consume(): Int {
        return channel.receive()
    }
}

suspend fun coroutineWork(channel: Channel<Int>, sampleClass: SampleClass) {
    repeat(10) { 
        sampleClass.produce(it)
    }
    println("All items produced.")
}

fun functionOne(channel: Channel<Int>, sampleClass: SampleClass) {
    println("Starting function one")
    runBlocking {
        launch {
            coroutineWork(channel, sampleClass)
        }
    }
}

fun functionTwo(sampleClass: SampleClass) {
    println("Starting function two")
    runBlocking {
        launch {
            repeat(10) {
                val item = sampleClass.consume()
                println("Consumed: $item")
            }
        }
    }
}

fun main(): Unit{
    val channel = Channel<Int>()
    val sampleClass = SampleClass(channel)

    functionOne(channel, sampleClass)
    functionTwo(sampleClass)
}

class RunChecker560: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}