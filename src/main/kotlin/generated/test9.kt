/* 
{"deadlock":false,"nFunctions":5,"nCoroutines":4,"nChannels":7,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 7 different channels
- 4 different coroutines
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
package org.example.generated.test9
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class ClassA(val intChannel: Channel<Int>) {
    suspend fun produceInts() {
        for (i in 1..5) {
            intChannel.send(i)
        }
        intChannel.close()
    }
}

class ClassB(val stringChannel: Channel<String>) {
    suspend fun produceStrings() {
        for (i in listOf("one", "two", "three", "four", "five")) {
            stringChannel.send(i)
        }
        stringChannel.close()
    }
}

class ClassC(val combinedChannel: Channel<Pair<Int, String>>) {
    suspend fun combine(intChannel: Channel<Int>, stringChannel: Channel<String>) {
        val intList = mutableListOf<Int>()
        val stringList = mutableListOf<String>()

        for (i in intChannel) {
            intList.add(i)
        }
        for (s in stringChannel) {
            stringList.add(s)
        }

        for (i in intList.indices) {
            combinedChannel.send(Pair(intList[i], stringList[i]))
        }
        combinedChannel.close()
    }
}

class ClassD(val processedChannel: Channel<String>) {
    suspend fun processPairs(combinedChannel: Channel<Pair<Int, String>>) {
        for (pair in combinedChannel) {
            processedChannel.send("${pair.first}-${pair.second}")
        }
        processedChannel.close()
    }
}

class ClassE {
    fun printProcessed(processedChannel: Channel<String>) {
        runBlocking {
            for (item in processedChannel) {
                println(item)
            }
        }
    }
}

fun main(): Unit{
    runBlocking {
        val intChannel = Channel<Int>()
        val stringChannel = Channel<String>()
        val combinedChannel = Channel<Pair<Int, String>>()
        val processedChannel = Channel<String>()

        launch {
            ClassA(intChannel).produceInts()
        }

        launch {
            ClassB(stringChannel).produceStrings()
        }

        launch {
            ClassC(combinedChannel).combine(intChannel, stringChannel)
        }

        launch {
            ClassD(processedChannel).processPairs(combinedChannel)
        }

        ClassE().printProcessed(processedChannel)
    }
}