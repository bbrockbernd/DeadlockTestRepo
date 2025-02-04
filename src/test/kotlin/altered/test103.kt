/* 
{"deadlock":false,"nFunctions":7,"nCoroutines":7,"nChannels":7,"nClasses":1}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 7 different functions
- 7 different channels
- 7 different coroutines
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
package org.example.altered.test103
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Worker {
    val numbersChannel = Channel<Int>()
    val resultsChannel = Channel<Int>()
    val doubledChannel = Channel<Int>()
    val summedChannel = Channel<Int>()
    val primesChannel = Channel<Int>()
    val processedChannel = Channel<Int>()
    val finalizedChannel = Channel<String>()

    suspend fun produceNumbers() {
        for (i in 1..10) {
            numbersChannel.send(i)
        }
        numbersChannel.close()
    }

    suspend fun doubleNumbers() {
        for (i in numbersChannel) {
            doubledChannel.send(i * 2)
        }
        doubledChannel.close()
    }

    suspend fun sumNumbers() {
        var sum = 0
        for (i in doubledChannel) {
            sum += i
        }
        summedChannel.send(sum)
        summedChannel.close()
    }

    suspend fun generatePrimes() {
        for (i in 2..20) {
            if (isPrime(i)) {
                primesChannel.send(i)
            }
        }
        primesChannel.close()
    }

    fun isPrime(num: Int): Boolean {
        if (num <= 1) return false
        for (i in 2 until num) {
            if (num % i == 0) return false
        }
        return true
    }

    suspend fun processResults() {
        val sum = summedChannel.receive()
        for (prime in primesChannel) {
            processedChannel.send(sum * prime)
        }
        processedChannel.close()
    }

    suspend fun finalizeResults() {
        for (result in processedChannel) {
            finalizedChannel.send("Result: $result")
        }
        finalizedChannel.close()
    }
}

fun main(): Unit= runBlocking {
    val worker = Worker()

    launch { worker.produceNumbers() }
    launch { worker.doubleNumbers() }
    launch { worker.sumNumbers() }
    launch { worker.generatePrimes() }
    launch { worker.processResults() }
    launch { worker.finalizeResults() }

    for (result in worker.finalizedChannel) {
        println(result)
    }
}

class RunChecker103: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}