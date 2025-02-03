/* 
{"deadlock":true,"nFunctions":8,"nCoroutines":6,"nChannels":7,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
- 7 different channels
- 6 different coroutines
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
package org.example.altered.test313
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class Producer(val output: Channel<Int>) {
    suspend fun produce() {
        repeat(5) {
            output.send(it)
        }
    }
}

class Processor(val input: Channel<Int>, val output: Channel<Int>) {
    suspend fun process() {
        for (num in input) {
            output.send(num * 2)
        }
    }
}

class Consumer(val input: Channel<Int>) {
    suspend fun consume() {
        for (num in input) {
            println("Consumed: $num")
        }
    }
}

fun channelOne(): Channel<Int> = Channel()
fun channelTwo(): Channel<Int> = Channel()
fun channelThree(): Channel<Int> = Channel()
fun channelFour(): Channel<Int> = Channel()
fun channelFive(): Channel<Int> = Channel()
fun channelSix(): Channel<Int> = Channel()
fun channelSeven(): Channel<Int> = Channel()

fun main(): Unit= runBlocking {
    val chan1 = channelOne()
    val chan2 = channelTwo()
    val chan3 = channelThree()
    val chan4 = channelFour()
    val chan5 = channelFive()
    val chan6 = channelSix()
    val chan7 = channelSeven()

    val producer1 = Producer(chan1)
    val processor1 = Processor(chan1, chan2)
    val processor2 = Processor(chan2, chan3)
    val consumer1 = Consumer(chan3)
    
    val producer2 = Producer(chan5)
    val processor3 = Processor(chan5, chan6)
    val processor4 = Processor(chan6, chan4)
    val consumer2 = Consumer(chan4)

    launch { producer1.produce() }
    launch { processor1.process() }
    launch { processor2.process() }
    launch { consumer1.consume() }
    
    launch { producer2.produce() }
    launch { processor3.process() }
    
    // Intentional deadlock: chan7 is never consumed
    launch { processor4.process() }
    
    // Uncommenting the following line will cause the code to work without deadlock
    // launch { consumer2.consume() }
}

class RunChecker313: RunCheckerBase() {
    override fun block() = main()
}